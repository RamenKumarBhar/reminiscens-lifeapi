package security;

import java.util.List;

import models.Participation;
import models.Relationship;

import play.mvc.Http;
import be.objectify.deadbolt.core.DeadboltAnalyzer;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDynamicResourceHandler;
import be.objectify.deadbolt.java.DeadboltHandler;

public class FriendsDynamicResourceHandler extends
		AbstractDynamicResourceHandler {

	@Override
	public boolean isAllowed(String name, String meta,
			DeadboltHandler deadboltHandler, Http.Context context) {
		Subject subject = deadboltHandler.getSubject(context);

		
		boolean allowed = false;
		if (DeadboltAnalyzer.hasRole(subject, "admin")) {
			allowed = true;
		} else {
				// a call to view profile is probably a get request, so
			// the query string is used to provide info
			
			String path = context.request().path();
			Long requestedResourceId = MyDynamicResourceHandler.getIdFromPath(path, meta);
			Long userId = new Long(subject.getIdentifier());
			Long userPersonId = models.User.read(userId).getPersonId();
			
			if(SecurityModelConstants.ID_FROM_PERSON.equals(meta)) {
				allowed = checkRelationship(userPersonId,requestedResourceId);	
			} else if (SecurityModelConstants.ID_FROM_STORY.equals(meta)) {
				List<Participation> protagonistList = getProtagonistList(requestedResourceId);
				allowed = checkRelationship(userPersonId,protagonistList);
			} else if (SecurityModelConstants.ID_FROM_USER.equals(meta)) {
				Long resourcePersonId = models.User.read(requestedResourceId).getPersonId();
				allowed = checkRelationship(userPersonId,resourcePersonId);
			} else if (SecurityModelConstants.ID_FROM_MEMENTO.equals(meta)) {
				Long storyId = getMementoStory(requestedResourceId);
				List<Participation> protagonistList = getProtagonistList(storyId);
				allowed = checkRelationship(userId, protagonistList);
			} else if (SecurityModelConstants.ID_FROM_CONTEXT.equals(meta)) {
				Long contextId = getPersonOfContext(requestedResourceId);
				allowed = checkCurators(userId,contextId);
			}
		}
		return allowed;
	}

	private Long getPersonOfContext(Long contextId) {
		return models.Context.read(contextId).getPersonForId();
	}

	private Long getMementoStory(Long mementoId) {
		return models.Memento.read(mementoId).getLifeStory().getLifeStoryId();
	}

	private boolean checkCurators(Long user, Long person) {
		return models.Relationship.isCuratorOf(user,person);
	}

	/**
	 * Get the list of participants in the lifeStoryId who are marked as protagonists
	 * @param lifeStoryId
	 * @return
	 */
	private List<Participation> getProtagonistList(Long lifeStoryId) {
		List<Participation> participants = models.Participation.protagonistsOfLifeStory(lifeStoryId);
		return participants;
	}

	/**
	 * check in there is a relationship between the user and the person
	 * @param user
	 * @param person
	 * @return
	 */
	private Boolean checkRelationship(Long user, Long person) {
		List<Relationship> relationships = models.Relationship.findRelationships(user,person) ;
		return relationships!= null && relationships.size()>0;
	}
	
	/**
	 * Check if there is at least one matching relationship between the requestor user and the participants 
	 * of a story
	 * 
	 * @param user
	 * @param participants
	 * @return
	 */
	private Boolean checkRelationship(Long user, List<Participation> participants) {
		Boolean allowed = false; 
		for (Participation participation : participants) {
			Long person = participation.getPerson().getPersonId();
			allowed = checkRelationship(user, person);
			if (allowed) {
				break;
			}
		}
		return allowed;
	}

}
