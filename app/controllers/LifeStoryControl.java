package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import models.User;
import be.objectify.deadbolt.java.actions.Dynamic;
import delegates.LifeStoryDelegate;
import delegates.MementoDelegate;
import enums.ResponseStatus;
import play.mvc.*;
import play.data.*;
import pojos.LifeStoryBean;
import pojos.MementoBean;
import pojos.ParticipationBean;
import pojos.PersonBean;
import pojos.ResponseStatusBean;
import security.SecurityModelConstants;
import utils.PlayDozerMapper;

public class LifeStoryControl extends Controller {

	static Form<LifeStoryBean> lifeStoryForm = Form.form(LifeStoryBean.class);
	static Form<MementoBean> mementoForm = Form.form(MementoBean.class);

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_PERSON)
	@Security.Authenticated(Secured.class)
	public static Result getPersonLifeStoryAll(Long id) {
		List<LifeStoryBean> bean = LifeStoryDelegate.getInstance()
				.getAllByPersonId(id);
		return bean != null ? ok(toJson(bean)) : notFound();
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_PERSON)
	public static Result getProtagonistLifeStoryAll(Long id) {
		List<LifeStoryBean> bean = LifeStoryDelegate.getInstance()
				.getAllByProtagonistId(id);
		return bean != null ? ok(toJson(bean)) : notFound();
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result getLifeStory(Long lsid) {
		LifeStoryBean bean = LifeStoryDelegate.getInstance().getLifeStory(lsid);
		return bean != null ? ok(toJson(bean)) : notFound();
	}

	@SuppressWarnings("unused")
	@Security.Authenticated(Secured.class)
	public static Result createLifeStory() {
		Form<LifeStoryBean> filledForm = lifeStoryForm.bindFromRequest();

		String userEmail = session().get("pa.u.id");
		User user = User.getByEmail(userEmail);
		PersonBean personBean = PlayDozerMapper.getInstance().map(
				user.getPerson(), PersonBean.class);

		if (user != null) {
			if (filledForm.hasErrors()) {
				ResponseStatusBean res = new ResponseStatusBean(
						ResponseStatus.BADREQUEST,
						"Body of request misses some information or it is malformed");
				return badRequest(toJson(res));
			} else {
				LifeStoryBean lifeStoryBean = filledForm.get();
				lifeStoryBean.setContributorId(user.getUserId());
				List<ParticipationBean> receiveParticipants = lifeStoryBean
						.getParticipationList();

				Boolean userIsIn = false;
				if (receiveParticipants != null
						&& receiveParticipants.size() > 0) {
					// check that the user is already a participant of the story
					for (ParticipationBean participationBean : receiveParticipants) {
						Long participantId = participationBean.getPersonId();
						Long contributor = participationBean.getContributorId();
						userIsIn = personBean.getPersonId() == participantId;

						// fix missing contributor
						if (contributor == null) {
							participationBean
									.setContributorId(user.getUserId());
						}

						if (userIsIn) {
							break;
						}
					}
				}

				if (!userIsIn) {
					// add the user as a protagonist
					ParticipationBean part = new ParticipationBean();
					part.setContributorId(user.getUserId());
					part.setProtagonist(true);
					part.setPerson(personBean);
					part.setLifeStory(lifeStoryBean);
					lifeStoryBean.addParticipant(part);
				}

				lifeStoryBean = LifeStoryDelegate.getInstance().create(
						lifeStoryBean);
				return ok(toJson(lifeStoryBean));
			}
		} else {
			flash("error", "Missing file");
			ResponseStatusBean response = new ResponseStatusBean();
			response.setResponseStatus(ResponseStatus.UNAUTHORIZED);
			response.setStatusMessage("User is information is null");
			return unauthorized(toJson(response));
		}
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result updateLifeStory(Long lsid) {
		Form<LifeStoryBean> filledForm = lifeStoryForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			LifeStoryBean lifeStoryBean = filledForm.get();
			try {
				lifeStoryBean = LifeStoryDelegate.getInstance().update(
						lifeStoryBean, lsid);
				return ok(toJson(lifeStoryBean));
			} catch (Exception e) {
				ResponseStatusBean res = new ResponseStatusBean(
						ResponseStatus.NODATA, "Entity does not exist",
						e.getMessage());
				return badRequest(toJson(res));
			}
		}
	}

	@Dynamic(value = "OnlyMe", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result deleteLifeStory(Long lsid) {
		try {
			LifeStoryDelegate.getInstance().deleteLifeStory(lsid);
			ResponseStatusBean res = new ResponseStatusBean(ResponseStatus.OK,
					"Entity deleted with success");
			return ok(toJson(res));
		} catch (Exception e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, "Entity does not exist",
					e.getMessage());
			return badRequest(toJson(res));
		}
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result addParticipantToLifeStory(Long lsid, Long id) {
		try {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			Long newParticipationId = LifeStoryDelegate.getInstance().addParticipant(lsid, id,
					user.getUserId(), false);
			ResponseStatusBean res = new ResponseStatusBean(ResponseStatus.OK,
					"New participant added with success");
			res.setNewResourceId(newParticipationId);
			return ok(toJson(res));
		} catch (Exception e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, "LifeStory does not exist",
					e.getMessage());
			return badRequest(toJson(res));
		}
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result addProtagonistToLifeStory(Long lsid, Long id) {
		try {
			String userEmail = session().get("pa.u.id");
			User user = User.getByEmail(userEmail);
			LifeStoryDelegate.getInstance().addParticipant(lsid, id,
					user.getUserId(), true);
			ResponseStatusBean res = new ResponseStatusBean(ResponseStatus.OK,
					"New participant added with success");
			return ok(toJson(res));
		} catch (Exception e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, "LifeStory does not exist",
					e.getMessage());
			return badRequest(toJson(res));
		}
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result deletePersonFromLifeStory(Long lsid, Long id) {
		try {
			LifeStoryDelegate.getInstance().deleteParticipant(lsid, id);
			ResponseStatusBean res = new ResponseStatusBean(ResponseStatus.OK,
					"Entity deleted with success");
			return ok(toJson(res));
		} catch (Exception e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, "Entity does not exist",
					e.getMessage());
			return badRequest(toJson(res));
		}
	}
	
	@Dynamic(value = "OnlyMe", meta = SecurityModelConstants.ID_FROM_PERSON)
	public static Result deleteProtagonistFromLifeStory(Long lsid, Long id) {
		try {
			LifeStoryDelegate.getInstance().deleteParticipant(lsid, id);
			ResponseStatusBean res = new ResponseStatusBean(ResponseStatus.OK,
					"Entity deleted with success");
			return ok(toJson(res));
		} catch (Exception e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, "Entity does not exist",
					e.getMessage());
			return badRequest(toJson(res));
		}
	}
	
	@Dynamic(value = "OnlyMe", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result addMementoToLifeStory(Long lsid, Long mid) {
		MementoBean mementoBean = MementoDelegate.getInstance().addMementoToLifeStory(lsid, mid);
		
		if (mementoBean == null) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, "Entity does not exist");
			return badRequest(toJson(res));
		} else {
		return ok(toJson(mementoBean));
		}
	}

	@Dynamic(value = "OnlyMe", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result addNewMementoToLifeStory(Long lsid) {
		Form<MementoBean> filledForm = mementoForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			MementoBean mementoBean = filledForm.get();
			mementoBean.setLifeStoryId(lsid);
			MementoDelegate.getInstance().create(mementoBean);
			return ok(toJson(mementoBean));
		}
	}

}
