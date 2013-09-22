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

public class CuratorsDynamicResourceHandler extends
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
			Long requestedResourceId = MyDynamicResourceHandler.getIdFromPath(
					path, meta);

			Long userId = new Long(subject.getIdentifier());
			Long userPersonId = models.User.read(userId).getPersonId();
			Logger.debug("Checking relationship of...");
			Logger.debug("--> userId = " + userId);
			Logger.debug("--> userPersonId = " + userPersonId);
			Logger.debug("--> requestedResourceId= " + requestedResourceId);
			Logger.debug("--> type of resource= " + meta);

			if (SecurityModelConstants.ID_FROM_PERSON.equals(meta)) {
				// allow to myself or my list of curators
				Logger.debug("--> Check if " + userPersonId + " is curator of (or himself) "
						+ requestedResourceId);
				allowed = checkCurators(userPersonId, requestedResourceId);
			} else if (SecurityModelConstants.ID_FROM_CONTEXT.equals(meta)) {
				// allow to myself or my list of contacts
				Long personId = getPersonOfContext(requestedResourceId);
				Logger.debug("--> Check if " + userPersonId + " is curator of (or himself) "
								+ personId);
				allowed = checkCurators(userPersonId, personId);
			}
		}
		return allowed;
	}

	private Long getPersonOfContext(Long contextId) {
		return models.Context.read(contextId).getPersonForId();
	}

	private boolean checkCurators(Long requestor, Long requested) {
		Boolean self = requestor == requested;
		return self ? self : models.Relationship.isCuratorOf(requestor,
				requested);
	}

	/**
	 * check in there is a relationship between the user and the person. if both
	 * are the same, return true
	 * 
	 * @param requestor
	 * @param requested
	 * @return
	 */
	private Boolean checkRelationship(Long requestor, Long requested) {
		if (requestor == requested) {
			// if we are talking of the same person, return true
			return true;
		} else {
			List<Relationship> relationships = models.Relationship
					.findRelationships(requestor, requested);
			return relationships != null && relationships.size() > 0;
		}
	}

	/**
	 * Check if there is at least one matching relationship between the
	 * requestor user and the participants of a story
	 * 
	 * @param user
	 * @param participants
	 * @return
	 */
	@SuppressWarnings("unused")
	private Boolean checkRelationship(Long user,
			List<Participation> participants) {
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
