package controllers;

import models.User;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import delegates.ContextDelegate;
import enums.ResponseStatus;
import exceptions.EntityDoesNotExist;
import exceptions.NotEnoughInformation;
import play.data.Form;
import play.mvc.*;
import pojos.ContextBean;
import pojos.ContributedMementoBean;
import pojos.ResponseStatusBean;
import static play.libs.Json.toJson;

public class ContextControl extends Controller {

	static Form<ContributedMementoBean> contributedForm = Form
			.form(ContributedMementoBean.class);

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
		try {
			ContextBean result = ContextDelegate.getInstance()
					.refreshContextForDecadeAndCity(cid,decade,cityId);
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

	public static Result refreshContextForDecade(Long cid, Long decade) {
		try {
			ContextBean result = ContextDelegate.getInstance()
					.refreshContextForDecade(cid,decade);
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

	public static Result refreshContext(Long cid) {
		try {
			ContextBean result = ContextDelegate.getInstance()
					.refreshContext(cid);
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

	public static Result getContext(Long cid) {
		ContextBean result = ContextDelegate.getInstance().getContext(cid);
		return ok(toJson(result));
	}

	public static Result deleteContext(Long cid) {
		try {
			ContextDelegate.getInstance().deleteContext(cid);
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

	public static Result getContextForDecade(Long cid, Long decade) {
		ContextBean result = ContextDelegate.getInstance().getContextForDecade(
				cid, decade);
		return ok(toJson(result));
	}

	public static Result getContextForDecadeAndCity(Long cid, Long decade,
			Long cityId) {
		ContextBean result = ContextDelegate.getInstance()
				.getContextForDecadeAndCity(cid, decade, cityId);
		return ok(toJson(result));
	}

	public static Result getContextFilterCategory(Long cid, String cat) {
		ContextBean result = ContextDelegate.getInstance()
				.getContextByCategory(cid, cat);
		return ok(toJson(result));
	}

	public static Result createContextMemento(Long cid) {
		Form<ContributedMementoBean> filledForm = contributedForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			try {
				ContributedMementoBean contributed = filledForm.get();
				ContextBean result = ContextDelegate.getInstance()
						.addMementoToContext(cid, contributed);
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

	public static Result updateContextMemento(Long cid, Long mid) {
		Form<ContributedMementoBean> filledForm = contributedForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			try {
				ContributedMementoBean contributed = filledForm.get();
				ContextBean result = ContextDelegate.getInstance()
						.updatedContextMemento(cid, mid, contributed);
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

	public static Result deleteContextMemento(Long cid, Long mid) {
		try {
			ContextDelegate.getInstance().deleteContextMemento(cid, mid);
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


	public static Result createContributedMemento() {
		Form<ContributedMementoBean> filledForm = contributedForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			try {
				ContributedMementoBean contributed = filledForm.get();
				ContextBean result = ContextDelegate.getInstance()
						.createContributedMemento(contributed);
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
