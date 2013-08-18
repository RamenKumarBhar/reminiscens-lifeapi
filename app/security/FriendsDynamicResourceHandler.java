package security;

import java.util.List;

import play.Logger;
import models.Participation;
import models.Relationship;

import play.mvc.Http;
import be.objectify.deadbolt.core.DeadboltAnalyzer;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDynamicResourceHandler;
import be.objectify.deadbolt.java.DeadboltHandler;
//import play.Logger;

public class FriendsDynamicResourceHandler extends
		AbstractDynamicResourceHandler {

	@Override
	public boolean isAllowed(String name, String meta,
			DeadboltHandler deadboltHandler, Http.Context context) {
		Subject subject = deadboltHandler.getSubject(context);
		
		boolean allowed = false;
		if (DeadboltAnalyzer.hasRole(subject, "ADMIN")) {
			Logger.debug("Requested by an ADMIN...");
			allowed = true;
		} else {
			// a call to view profile is probably a get request, so
			// the query string is used to provide info
			
			String path = context.request().path();
			Long requestedResourceId = MyDynamicResourceHandler.getIdFromPath(path, meta);
			
			Long userId = new Long(subject.getIdentifier());
			Long userPersonId = models.User.read(userId).getPersonId();
			Logger.debug("Checking relationship of...");
			Logger.debug("--> userId = "+userId);
			Logger.debug("--> userPersonId = "+userPersonId);
			Logger.debug("--> requestedResourceId= "+requestedResourceId);
			Logger.debug("--> type of resource= "+meta);

			if(SecurityModelConstants.ID_FROM_PERSON.equals(meta)) {
				// allow to myself or my list of contacts
				allowed=checkRelationship(userPersonId,requestedResourceId);	
			} else if (SecurityModelConstants.ID_FROM_STORY.equals(meta)) {
				// allow to myself or my list of contacts
				List<Participation> participants = getParticipantList(requestedResourceId);
				allowed = checkRelationship(userPersonId,participants);
			} else if (SecurityModelConstants.ID_FROM_USER.equals(meta)) {
				// allow to myself or my list of contacts
				Long resourcePersonId = models.User.read(requestedResourceId).getPersonId();
				allowed = checkRelationship(userPersonId,resourcePersonId);
			} else if (SecurityModelConstants.ID_FROM_MEMENTO.equals(meta)) {
				// allow to myself or my list of contacts
				Long storyId = getMementoStory(requestedResourceId);
				List<Participation> participants = getParticipantList(storyId);
				allowed = checkRelationship(userId, participants);
			} else if (SecurityModelConstants.ID_FROM_CONTEXT.equals(meta)) {
				// allow to myself or my list of contacts
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
	private List<Participation> getParticipantList(Long lifeStoryId) {
		List<Participation> participants = models.Participation.participationByLifeStory(lifeStoryId);
		return participants;
	}

	/**
	 * check in there is a relationship between the user and the person. if both are the same, return true
	 * @param requestor
	 * @param requested
	 * @return
	 */
	private Boolean checkRelationship(Long requestor, Long requested) {
		if (requestor == requested) {
		// if we are talking of the same person, return true
			return true;
		} else {
			List<Relationship> relationships = models.Relationship.findRelationships(requestor,requested) ;
			return relationships!= null && relationships.size()>0;
		}
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
