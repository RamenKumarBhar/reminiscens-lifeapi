package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import models.User;

import com.feth.play.module.pa.PlayAuthenticate;

import annotations.CustomRestrict;
import be.objectify.deadbolt.java.actions.Dynamic;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;

import delegates.UserDelegate;
import enums.MyRoles;
import enums.ResponseStatus;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;
import play.mvc.Http.Session;
import pojos.UserBean;
import pojos.ResponseStatusBean;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import providers.MyUsernamePasswordAuthProvider.MySignup;
import security.SecurityModelConstants;
import utils.PlayDozerMapper;

public class UserControl extends Controller {

	static Form<UserBean> userForm = Form.form(UserBean.class);

	public static Result doLogin() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			// return badRequest(login.render(filledForm));
			return badRequest();
		} else {
			// Everything was filled
			return MyUsernamePasswordAuthProvider.handleLogin(ctx());
		}
	}
	
	// TODO 1. Create the Birth story immediately after the signup
	// TODO 2. Create the seminal context immediately after signup
	public static Result doSignup() {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		final Form<MySignup> filledForm = MyUsernamePasswordAuthProvider.SIGNUP_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill everything properly
			// return badRequest(signup.render(filledForm));
			ResponseStatusBean response = new ResponseStatusBean();
			response.setResponseStatus(ResponseStatus.BADREQUEST);
			response.setStatusMessage("play.authenticate.filledFromHasErrors:"
					+ filledForm.errorsAsJson());
			return badRequest(toJson(response));
		} else {
			// Everything was filled correctly
			// Do something with your part of the form before handling the user
			// signup
			return MyUsernamePasswordAuthProvider.handleSignup(ctx());
		}
	}

	// TODO implement our own logout
	public static Result doLogout() {
		return com.feth.play.module.pa.controllers.Authenticate.logout();
	}

 	@Security.Authenticated(Secured.class)
	@SubjectPresent	
	@Dynamic(value="OnlyMe", meta=SecurityModelConstants.ID_FROM_USER)
	public static Result updateUser(Long uid) {
		Form<UserBean> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			UserBean userBean = filledForm.get();
			try {
				UserDelegate.getInstance().update(userBean, uid);
				return ok(toJson(userBean));
			} catch (Exception e) {
				ResponseStatusBean res = new ResponseStatusBean(
						ResponseStatus.NODATA, "Entity does not exist",
						e.getMessage());
				return badRequest(toJson(res));
			}
		}
	}

	@Dynamic(value="OnlyMe", meta=SecurityModelConstants.ID_FROM_USER)
	public static Result deleteUser(Long uid) {
		try {
			UserDelegate.getInstance().deleteUser(uid);
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

	
	@Dynamic(value="OnlyMe", meta=SecurityModelConstants.ID_FROM_USER)
	public static Result deleteUserForce(Long uid) {
		try {
			UserDelegate.getInstance().deleteUserForce(uid);
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
	
	@CustomRestrict(value = {MyRoles.ADMIN}, config = @Restrict({}))
	public static Result getUsers() {
		List<UserBean> listUsers = UserDelegate.getInstance().getAll();
		return listUsers != null ? ok(toJson(listUsers)) : notFound();
	}

	@Dynamic(value="OnlyMe", meta=SecurityModelConstants.ID_FROM_USER)
	public static Result getUser(Long uid) {
		UserBean bean = UserDelegate.getInstance().getUser(uid);
		return bean != null ? ok(toJson(bean)) : notFound();
	}

	@Security.Authenticated(Secured.class)
	public static Result getUserByEmail() {
		Form<UserBean> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			UserBean userBean = filledForm.get();
			UserBean bean = UserDelegate.getInstance().getUserByEmail(
					userBean.getEmail());
			return bean != null ? ok(toJson(bean)) : notFound();
		}
	}
	
	public static Result onLoginUserNotFound() {
		ResponseStatusBean response = new ResponseStatusBean();
		response.setResponseStatus(ResponseStatus.NODATA);
		response.setStatusMessage(Messages
				.get("playauthenticate.password.login.unknown_user_or_pw"));
		return notFound(toJson(response));
		// return notFound(Messages
		// .get("playauthenticate.password.login.unknown_user_or_pw"));
	}

	public static Result oAuthDenied(final String providerKey) {
		com.feth.play.module.pa.controllers.Authenticate.noCache(response());
		flash(Application.FLASH_ERROR_KEY,
				"You need to accept the OAuth connection in order to use this website!");
		return redirect(routes.Application.index());
	}

	public static User getLocalUser(final Session session) {
		final User localUser = User.findByAuthUserIdentity(PlayAuthenticate
				.getUser(session));
		return localUser;
	}
	
	// TODO verify old endpoints, cleanup
	@Security.Authenticated(Secured.class)
	public static Result createUser() {
		Form<UserBean> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			ResponseStatusBean res = new ResponseStatusBean(
					ResponseStatus.BADREQUEST,
					"Body of request misses some information or it is malformed");
			return badRequest(toJson(res));
		} else {
			UserBean userBean = filledForm.get();
			UserDelegate.getInstance().create(userBean);
			return ok(toJson(userBean));
		}
	}
	
	@CustomRestrict(value = {MyRoles.MEMBER}, config = @Restrict({}))
	public static Result profile() {
		final User localUser = getLocalUser(session());
		UserBean bean = PlayDozerMapper.getInstance().map(localUser, UserBean.class);
		return ok(toJson(bean));
	}
}
