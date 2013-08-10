package controllers;

import java.util.List;

import annotations.CustomRestrict;
import be.objectify.deadbolt.java.actions.Dynamic;
import be.objectify.deadbolt.java.actions.Restrict;


import play.mvc.*;
import play.data.*;
import pojos.MentionPersonBean;
import pojos.PersonBean;
import pojos.RelationshipBean;
import pojos.ResponseStatusBean;
import security.SecurityModelConstants;
import static play.libs.Json.toJson;
import enums.MyRoles;
import delegates.PersonDelegate;
import enums.ResponseStatus;

public class PersonControl extends Controller {

	static Form<PersonBean> personForm = Form.form(PersonBean.class);
	static Form<RelationshipBean> relationshipForm = Form.form(RelationshipBean.class);
	static Form<MentionPersonBean> mentionPersonForm = Form.form(MentionPersonBean.class);

	/** 
	 * Get a complete list of people mentioned in reminiscens
	 * @return
	 */
	@CustomRestrict(value = {MyRoles.ADMIN}, config = @Restrict({}))
	public static Result getPersonAll() {
		List<PersonBean> lp = PersonDelegate.getInstance().getAll();
		return lp != null ? ok(toJson(lp)) : notFound();
	}

	@Dynamic(value="FriendOf", meta=SecurityModelConstants.ID_FROM_PERSON)
	public static Result getPerson(Long id) {
		PersonBean bean = PersonDelegate.getInstance().getPerson(id);
		return bean != null ? ok(toJson(bean)) : notFound();
	}

	@CustomRestrict(value = {MyRoles.MEMBER}, config = @Restrict({}))
	public static Result createPerson() {
		Form<PersonBean> filledForm = personForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			PersonBean personBean = filledForm.get();
			PersonDelegate.getInstance().create(personBean);
			return ok(toJson(personBean));
		}
	}

	@Dynamic(value="OnlyMe", meta=SecurityModelConstants.ID_FROM_PERSON)
	public static Result updatePerson(Long id) {
		Form<PersonBean> filledForm = personForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			PersonBean personBean = filledForm.get();
			try {
				PersonDelegate.getInstance().update(personBean, id);
				return ok(toJson(personBean));
			} catch (Exception e) {
				ResponseStatusBean res = new ResponseStatusBean(
						ResponseStatus.NODATA, "Entity does not exist",
						e.getMessage());
				return badRequest(toJson(res));
			}
		}
	}
	
	@Dynamic(value="OnlyMe", meta=SecurityModelConstants.ID_FROM_PERSON)
	// TODO eliminate this method, and leave only the deletion of user
	public static Result deletePerson(Long id) {
		try {
			PersonDelegate.getInstance().deletePerson(id);
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

	@Security.Authenticated(Secured.class)
	public static Result createMentionPerson() {
		Form<MentionPersonBean> filledForm = mentionPersonForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			MentionPersonBean mentionPersonBean = filledForm.get();
			PersonDelegate.getInstance().createPersonMention(mentionPersonBean);
			return ok(toJson(mentionPersonBean ));
		}
	}
}
