package controllers;

import models.User;
import be.objectify.deadbolt.java.actions.Dynamic;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import delegates.ContextDelegate;
import enums.ResponseStatus;
import exceptions.EntityDoesNotExist;
import exceptions.NotEnoughInformation;
import play.mvc.*;
import pojos.ContextBean;
import pojos.PersonBean;
import pojos.ResponseStatusBean;
import security.SecurityModelConstants;
import utils.PlayDozerMapper;
import static play.libs.Json.toJson;

public class ContextControl extends Controller {
	public static Result initContextForPersonAndDecadeAndCity(Long id,
			Long decade, Long cityId) {
		try {
			ContextBean result = ContextDelegate.getInstance()
					.initContextForPersonAndDecadeAndCity(id, decade, cityId);
			return ok(toJson(result));
		} catch (EntityDoesNotExist e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, e.getLocalizedMessage());
			return internalServerError(toJson(res));
		} catch (NotEnoughInformation e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST, e.getLocalizedMessage());
			return badRequest(toJson(res));
		}
	}

	public static Result initContextForPersonAndDecade(Long id, Long decade) {
		try {
			ContextBean result = ContextDelegate.getInstance()
					.initContextForPersonAndDecade(id, decade);
			return ok(toJson(result));
		} catch (EntityDoesNotExist e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, e.getLocalizedMessage());
			return internalServerError(toJson(res));
		} catch (NotEnoughInformation e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST, e.getLocalizedMessage());
			return badRequest(toJson(res));
		}
	}

	public static Result initContextForPerson(Long id) {
		try {
			ContextBean result = ContextDelegate.getInstance()
					.initContextForPerson(id);
			return ok(toJson(result));
		} catch (EntityDoesNotExist e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, e.getLocalizedMessage());
			return internalServerError(toJson(res));
		} catch (NotEnoughInformation e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST, e.getLocalizedMessage());
			return badRequest(toJson(res));
		}
	}

	public static Result refreshContextForDecadeAndCity(Long cid, Long decade,
			Long cityId) {
		/** @TODO */
		return TODO;
	}

	public static Result refreshContextForDecade(Long cid, Long decade) {
		/** @TODO */
		return TODO;
	}

	public static Result refreshContext(Long cid) {
		/** @TODO */
		return TODO;
	}

	public static Result getContext(Long cid) {
		ContextBean result = ContextDelegate.getInstance().getContext(cid);
		return ok(toJson(result));
	}

	public static Result getContextForDecade(Long cid, Long decade) {
		/** @TODO */
		return TODO;
	}

	public static Result getContextForDecadeAndCity(Long cid, Long decade,
			Long cityId) {
		/** @TODO */
		return TODO;
	}

	public static Result getContextFilterCategory(Long cid, String cat) {
		/** @TODO */
		return TODO;
	}

	public static Result createContextMemento(Long cid) {
		/** @TODO */
		return TODO;
	}

	public static Result updateContextMemento(Long cid, Long mid) {
		/** @TODO */
		return TODO;
	}

	public static Result deleteContextMemento(Long cid, Long mid) {
		/** @TODO */
		return TODO;
	}

	public static Result getContextCurators(Long cid) {
		/** @TODO */
		return TODO;
	}

	public static Result getContextMemento(Long cid, Long mid) {
		/** @TODO */
		return TODO;
	}

	@SubjectPresent
	public static Result initContext() {
		String userEmail = session().get("pa.u.id");
		User user = User.getByEmail(userEmail);
		Long id = user.getPersonId();
		try {
			ContextBean result = ContextDelegate.getInstance()
					.initContextForPerson(id);
			return ok(toJson(result));
		} catch (EntityDoesNotExist e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.NODATA, e.getLocalizedMessage());
			return internalServerError(toJson(res));
		} catch (NotEnoughInformation e) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST, e.getLocalizedMessage());
			return badRequest(toJson(res));
		}
	}
}
