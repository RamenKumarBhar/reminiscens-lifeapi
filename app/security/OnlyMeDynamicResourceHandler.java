package security;

import java.util.List;

import models.LifeStory;
import models.Participation;

import play.mvc.Http;
import be.objectify.deadbolt.core.DeadboltAnalyzer;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDynamicResourceHandler;
import be.objectify.deadbolt.java.DeadboltHandler;

public class OnlyMeDynamicResourceHandler extends
		AbstractDynamicResourceHandler {

	@Override
	public boolean isAllowed(String name, String meta,
			DeadboltHandler deadboltHandler, Http.Context context) {
		Subject subject = deadboltHandler.getSubject(context);

		
		boolean allowed = false;
		if (DeadboltAnalyzer.hasRole(subject, "ADMIN")) {
			allowed = true;
		} else {
				// a call to view profile is probably a get request, so
			// the query string is used to provide info
			
			String path = context.request().path();
			Long requestedResourceId = MyDynamicResourceHandler.getIdFromPath(path, meta);
			Long userId = new Long(subject.getIdentifier());
			Long userPersonId = models.User.read(userId).getPersonId();
			
			if(SecurityModelConstants.ID_FROM_PERSON.equals(meta)) {
				allowed = userPersonId == requestedResourceId;	
			} else if (SecurityModelConstants.ID_FROM_STORY.equals(meta)) {
				LifeStory story = models.LifeStory.read(requestedResourceId);
				allowed = story != null && story.getContributorId() == userId && checkThatStoryIsMine(userPersonId,story);
			} else if (SecurityModelConstants.ID_FROM_MEMENTO.equals(meta)) {
				LifeStory story = getMementoStory(requestedResourceId);
				allowed = story!=null && story.getContributorId() == userId && checkThatStoryIsMine(userPersonId, story);
			}  else if (SecurityModelConstants.ID_FROM_RELATIONSHIPS.equals(meta)) {
				// TODO implement relationships security checks
				allowed = true;
			}
		}
		return allowed;
	}

	private LifeStory getMementoStory(Long mementoId) {
		return models.Memento.read(mementoId).getLifeStory();
	}

	/**
	 * Get the list of participants in the lifeStoryId who are marked as protagonists
	 * @param lifeStoryId
	 * @return
	 */
	private Boolean checkThatStoryIsMine(Long person, LifeStory story) {
		Boolean allowed = false;
		for (Participation participant : story.getParticipationList()) {
			allowed = participant.getPerson().getPersonId() == person;
			if (allowed) {
				break;
			}
		}
		return allowed;
	}
}
