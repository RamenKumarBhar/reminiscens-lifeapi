package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import annotations.CustomRestrict;
import be.objectify.deadbolt.java.actions.Dynamic;
import be.objectify.deadbolt.java.actions.Restrict;

import delegates.MementoDelegate;
import enums.MyRoles;
import enums.ResponseStatus;
import play.data.Form;
import play.mvc.*;
import pojos.MementoBean;
import pojos.MementoParticipationBean;
import pojos.MentionPersonBean;
import pojos.ResponseStatusBean;
import security.SecurityModelConstants;

public class MementoControl extends Controller {
	static Form<MementoBean> mementoForm = Form.form(MementoBean.class);
	static Form<MentionPersonBean> mentionPersonForm = Form.form(MentionPersonBean.class);
	
	@CustomRestrict(value = {MyRoles.ADMIN}, config = @Restrict({}))
	public static Result getAllMemento() {
		List<MementoBean> lp = MementoDelegate.getInstance().getAll();
		return lp != null ? ok(toJson(lp)) : notFound();
	}
	
	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_PERSON)
	public static Result getAllPersonMemento(Long personId) {
		List<MementoBean> lp = MementoDelegate.getInstance()
				.getAllPersonMemento(personId);
		return lp != null ? ok(toJson(lp)) : notFound();
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_PERSON)
	public static Result getPersonMementoByParticipationType(Long personId, String type) {
		if (type.equals("protagonist")) {
			return getPersonProtagonistMemento(personId);
		} else if (type.equals("mention")) {
			return getPersonMentionMemento(personId);
		} else {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA,
					"Mementos with that type of participation ("+type+") does not exist");
			return notFound(toJson(res));
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
	
	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result getMemento(Long lsid, Long id) {
		MementoBean bean = MementoDelegate.getInstance().getMemento(id);
		return bean != null ? ok(toJson(bean)) : notFound();
	}
	
	@Dynamic(value = "OnlyMe", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result updateMemento(Long lsid, Long id) {
		Form<MementoBean> filledForm = mementoForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			MementoBean mementoBean = filledForm.get();
			try {
				mementoBean.setLifeStoryId(lsid);
				mementoBean.setMementoId(id);
				MementoDelegate.getInstance().update(mementoBean);
				return ok(toJson(mementoBean));
			} catch (Exception e) {
				ResponseStatusBean res = new ResponseStatusBean(
						ResponseStatus.NODATA, "Entity does not exist",
						e.getMessage());
				return badRequest(toJson(res));
			}
		}
	}
	
	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result deleteMemento(Long lsid, Long id) {
		try {
			MementoDelegate.getInstance().deleteMemento(id);
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
	public static Result addMementoParticipant(Long lsid, Long id) {
		Form<MentionPersonBean> filledForm = mentionPersonForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			MentionPersonBean mentionPerson = filledForm.get();
			try {
				MementoDelegate.getInstance().addMementoMentionParticipant(id,mentionPerson);
				return ok(toJson(mentionPerson));
			} catch (Exception e) {
				ResponseStatusBean res = new ResponseStatusBean(
						ResponseStatus.NODATA, "Entity does not exist",
						e.getMessage());
				return badRequest(toJson(res));
			}
		}
	}
	
	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result addMementoParticipantByPersonId(Long lsid, Long id, Long pid) {
		MementoParticipationBean response;
		try {
			response = MementoDelegate.getInstance()
					.addMementoParticipantByPersonId(id, pid);
		} catch (NullPointerException e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA,
					"Relationship not created because some of the entities involved do no exist",
					e.toString());
			return badRequest(toJson(res));
		}

		return ok(toJson(response));
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_STORY)
	public static Result deleteMementoParticipantByPersonId(Long lsid, Long id, Long pid) {
		try {
			MementoDelegate.getInstance()
					.deleteMementoParticipantByPersonId(id, pid);
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.OK,
					"Participant in Memento was deleted with success");
			return ok(toJson(res));
		} catch (NullPointerException e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA,
					"Relationship not deleted because some of the entities involved do no exist",
					e.toString());
			return badRequest(toJson(res));
		}
	}

	
	
	// DEPRECATED 
	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_PERSON)
	public static Result getPersonProtagonistMemento(Long personId) {
		List<MementoBean> lp = MementoDelegate.getInstance()
				.getPersonProtagonistMemento(personId);
		return lp != null ? ok(toJson(lp)) : notFound();
	}

	@Dynamic(value = "FriendOf", meta = SecurityModelConstants.ID_FROM_PERSON)
	public static Result getPersonMentionMemento(Long personId) {
		List<MementoBean> lp = MementoDelegate.getInstance()
				.getPersonMentiontMemento(personId);
		return lp != null ? ok(toJson(lp)) : notFound();
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
	
	public static Result uploadMemento(Long id, Long mid) {
		/** @TODO */
		return TODO;
	}

	/*
	 * TODO
	 * 1. Delete participants from mementos
	 * 2. Fix update of memento that is not mapped to the MentionPerson table
	 * 3. Get rid of MementoParticipation classes
	 * 4. Improve adding PersonMention and mapping directly to a real person 
	 * 5. Find a way to avoid duplicates in Memento mentioned people
	 * 6. Fix Problem of MentionPerson not having an @Id
	 */
}
