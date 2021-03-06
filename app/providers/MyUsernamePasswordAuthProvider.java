package providers;

import static play.data.Form.form;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import models.LinkedAccount;
import models.TokenAction;
import models.TokenAction.Type;
import models.User;
import play.Application;
import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Call;
import play.mvc.Http.Context;
import play.mvc.Result;
import pojos.PersonBean;
import service.PlayAuthenticateLocal;

import com.feth.play.module.mail.Mailer.Mail.Body;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;

import controllers.routes;

public class MyUsernamePasswordAuthProvider
		extends
		UsernamePasswordAuthProvider<String, MyLoginUsernamePasswordAuthUser, MyUsernamePasswordAuthUser, MyUsernamePasswordAuthProvider.MyLogin, MyUsernamePasswordAuthProvider.MySignup> {

	private static final String SETTING_KEY_VERIFICATION_LINK_SECURE = SETTING_KEY_MAIL
			+ "." + "verificationLink.secure";
	private static final String SETTING_KEY_PASSWORD_RESET_LINK_SECURE = SETTING_KEY_MAIL
			+ "." + "passwordResetLink.secure";
	private static final String SETTING_KEY_LINK_LOGIN_AFTER_PASSWORD_RESET = "loginAfterPasswordReset";

	private static final String EMAIL_TEMPLATE_FALLBACK_LANGUAGE = "it";

	@Override
	protected List<String> neededSettingKeys() {
		final List<String> needed = new ArrayList<String>(
				super.neededSettingKeys());
		needed.add(SETTING_KEY_VERIFICATION_LINK_SECURE);
		needed.add(SETTING_KEY_PASSWORD_RESET_LINK_SECURE);
		needed.add(SETTING_KEY_LINK_LOGIN_AFTER_PASSWORD_RESET);
		return needed;
	}

	public static MyUsernamePasswordAuthProvider getProvider() {
		return (MyUsernamePasswordAuthProvider) PlayAuthenticate
				.getProvider(UsernamePasswordAuthProvider.PROVIDER_KEY);
	}

	public static class MyIdentity {

		public MyIdentity() {
		}

		public MyIdentity(final String email) {
			this.email = email;
		}

		@Required
		@Email
		public String email;

	}

	public static class MyLogin extends MyIdentity
			implements
			com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.UsernamePassword {

		@Required
		@MinLength(5)
		public String password;

		@Override
		public String getEmail() {
			return email;
		}

		@Override
		public String getPassword() {
			return password;
		}
	}

	public static class MySignup extends MyLogin {

		@Required
		@MinLength(5)
		private String repeatPassword;

		public String name;

		@Required
		private PersonBean person;

		private Long userId;

		private String profilePic;

		public String validate() {
			if (password == null || !password.equals(getRepeatPassword())) {
				// ResponseStatusBean response = ResponseStatusBean();
				// response.setResponseStatus(ResponseStatus.BADREQUEST);
				// response.setStatusMessage("playauthenticate.password.signup.error.passwords_not_same");
				// return
				return Messages
						.get("playauthenticate.password.signup.error.passwords_not_same");
			}
			return null;
		}

		public String getRepeatPassword() {
			return repeatPassword;
		}

		public void setRepeatPassword(String repeatPassword) {
			this.repeatPassword = repeatPassword;
		}

		public PersonBean getPerson() {
			return person;
		}

		public void setPerson(PersonBean person) {
			this.person = person;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getProfilePic() {
			return profilePic;
		}

		public void setProfilePic(String profilePic) {
			this.profilePic = profilePic;
		}
		
	}

	public static final Form<MySignup> SIGNUP_FORM = form(MySignup.class);
	public static final Form<MyLogin> LOGIN_FORM = form(MyLogin.class);

	public MyUsernamePasswordAuthProvider(Application app) {
		super(app);
	}

	protected Form<MySignup> getSignupForm() {
		return SIGNUP_FORM;
	}

	protected Form<MyLogin> getLoginForm() {
		return LOGIN_FORM;
	}

	@Override
	protected com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.SignupResult signupUser(
			final MyUsernamePasswordAuthUser user) {
		final User u = User.findByUsernamePasswordIdentity(user);
		if (u != null) {
			if (u.isEmailValidated()) {
				// This user exists, has its email validated and is active
				return SignupResult.USER_EXISTS;
			} else {
				// this user exists, is active but has not yet validated its
				// email
				return SignupResult.USER_EXISTS_UNVERIFIED;
			}
		}
		// The user either does not exist or is inactive - create a new one

		final User newUser = User.create(user);
		user.setUserId(newUser.getUserId());
		// Usually the email should be verified before allowing login, however
		// if you return
		// return SignupResult.USER_CREATED;
		// then the user gets logged in directly
		// return SignupResult.USER_CREATED_UNVERIFIED;

		// TODO verify that the email is correct and valid

		return SignupResult.USER_CREATED;
	}

	@Override
	protected com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.LoginResult loginUser(
			final MyLoginUsernamePasswordAuthUser authUser) {
		final User u = User.findByUsernamePasswordIdentity(authUser);
		if (u == null) {
			return LoginResult.NOT_FOUND;
		} else {
			// TODO every certain time, resend verification email
			// if (!u.isEmailValidated()) {
			// return LoginResult.USER_UNVERIFIED;
			// }
			//
			// {
			for (final LinkedAccount acc : u.getLinkedAccounts()) {
				if (getKey().equals(acc.getProviderKey())) {
					if (authUser.checkPassword(acc.getProviderUserId(),
							authUser.getPassword())) {
						// Password was correct
						return LoginResult.USER_LOGGED_IN;
					} else {
						// if you don't return here,
						// you would allow the user to have
						// multiple passwords defined
						// usually we don't want this
						return LoginResult.WRONG_PASSWORD;
					}
				}
			}
			return LoginResult.WRONG_PASSWORD;
			// }
		}
	}

	@Override
	protected Call userExists(final UsernamePasswordAuthUser authUser) {
		// find out how to return just json
		// return null;
		return routes.Signup.exists();
	}

	@Override
	protected Call userUnverified(final UsernamePasswordAuthUser authUser) {
		// find out how to return just json
		// return null;
		return routes.Signup.unverified();
	}

	@Override
	protected MyUsernamePasswordAuthUser buildSignupAuthUser(
			final MySignup signup, final Context ctx) {
		return new MyUsernamePasswordAuthUser(signup);
	}

	@Override
	protected MyLoginUsernamePasswordAuthUser buildLoginAuthUser(
			final MyLogin login, final Context ctx) {
		return new MyLoginUsernamePasswordAuthUser(login.getPassword(),
				login.getEmail());
	}

	@Override
	protected MyLoginUsernamePasswordAuthUser transformAuthUser(
			final MyUsernamePasswordAuthUser authUser, final Context context) {
		return new MyLoginUsernamePasswordAuthUser(authUser.getEmail());
	}

	@Override
	protected String getVerifyEmailMailingSubject(
			final MyUsernamePasswordAuthUser user, final Context ctx) {
		return Messages.get("playauthenticate.password.verify_signup.subject");
	}

	@Override
	protected String onLoginUserNotFound(final Context context) {
		// context.flash()
		// .put(controllers.Application.FLASH_ERROR_KEY,
		// Messages.get("playauthenticate.password.login.unknown_user_or_pw"));
		// return controllers.routes.Application.onLoginUserNotFound().url();
		return UsernamePasswordAuthProvider.LoginResult.NOT_FOUND.toString();
		// return super.onLoginUserNotFound(context);
	}

	@Override
	protected Body getVerifyEmailMailingBody(final String token,
			final MyUsernamePasswordAuthUser user, final Context ctx) {

		final boolean isSecure = getConfiguration().getBoolean(
				SETTING_KEY_VERIFICATION_LINK_SECURE);
		// TODO find out how to return just json
		// final String url = "";
		final String url = routes.Signup.verify(token).absoluteURL(
				ctx.request(), isSecure);

		final Lang lang = Lang.preferred(ctx.request().acceptLanguages());
		final String langCode = lang.code();

		final String html = getEmailTemplate(
				"views.html.account.signup.email.verify_email", langCode, url,
				token, user.getName(), user.getEmail());
		final String text = getEmailTemplate(
				"views.txt.account.signup.email.verify_email", langCode, url,
				token, user.getName(), user.getEmail());

		return new Body(text, html);
	}

	private static String generateToken() {
		return UUID.randomUUID().toString();
	}

	@Override
	protected String generateVerificationRecord(
			final MyUsernamePasswordAuthUser user) {
		return generateVerificationRecord(User.findByAuthUserIdentity(user));
	}

	protected String generateVerificationRecord(final User user) {
		final String token = generateToken();
		// Do database actions, etc.
		TokenAction.create(Type.EMAIL_VERIFICATION, token, user);
		return token;
	}

	protected String generatePasswordResetRecord(final User u) {
		final String token = generateToken();
		TokenAction.create(Type.PASSWORD_RESET, token, u);
		return token;
	}

	protected String getPasswordResetMailingSubject(final User user,
			final Context ctx) {
		return Messages.get("playauthenticate.password.reset_email.subject");
	}

	protected Body getPasswordResetMailingBody(final String token,
			final User user, final Context ctx) {

		// final boolean isSecure = getConfiguration().getBoolean(
		// SETTING_KEY_PASSWORD_RESET_LINK_SECURE);
		// TODO find out how to return just json
		final String url = "";
		// final String url = routes.Signup.resetPassword(token).absoluteURL(
		// ctx.request(), isSecure);

		final Lang lang = Lang.preferred(ctx.request().acceptLanguages());
		final String langCode = lang.code();

		final String html = getEmailTemplate(
				"views.html.account.email.password_reset", langCode, url,
				token, user.getPerson().getFirstname(), user.getEmail());
		final String text = getEmailTemplate(
				"views.txt.account.email.password_reset", langCode, url, token,
				user.getPerson().getFirstname(), user.getEmail());

		return new Body(text, html);
	}

	public void sendPasswordResetMailing(final User user, final Context ctx) {
		final String token = generatePasswordResetRecord(user);
		final String subject = getPasswordResetMailingSubject(user, ctx);
		final Body body = getPasswordResetMailingBody(token, user, ctx);
		mailer.sendMail(subject, body, getEmailName(user));
	}

	public boolean isLoginAfterPasswordReset() {
		return getConfiguration().getBoolean(
				SETTING_KEY_LINK_LOGIN_AFTER_PASSWORD_RESET);
	}

	protected String getVerifyEmailMailingSubjectAfterSignup(final User user,
			final Context ctx) {
		return Messages.get("playauthenticate.password.verify_email.subject");
	}

	protected String getEmailTemplate(final String template,
			final String langCode, final String url, final String token,
			final String name, final String email) {
		Class<?> cls = null;
		String ret = null;
		String locale = "";

		if (langCode.equals("it_IT") || langCode.equals("it-IT")) {
			locale = "it";
		} else if (langCode.equals("es_ES") || langCode.equals("es-ES")) {
			locale = "es";
		} else {
			locale = langCode;
		}

		try {
			cls = Class.forName(template + "_" + locale);
		} catch (ClassNotFoundException e) {
			Logger.warn("Template: '"
					+ template
					+ "_"
					+ locale
					+ "' was not found! Trying to use English fallback template instead.");
		}
		if (cls == null) {
			try {
				cls = Class.forName(template + "_"
						+ EMAIL_TEMPLATE_FALLBACK_LANGUAGE);
			} catch (ClassNotFoundException e) {
				Logger.error("Fallback template: '" + template + "_"
						+ EMAIL_TEMPLATE_FALLBACK_LANGUAGE
						+ "' was not found either!");
			}
		}
		if (cls != null) {
			Method htmlRender = null;
			try {
				htmlRender = cls.getMethod("render", String.class,
						String.class, String.class, String.class);
				ret = htmlRender.invoke(null, url, token, name, email)
						.toString();

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	protected Body getVerifyEmailMailingBodyAfterSignup(final String token,
			final User user, final Context ctx) {

		// final boolean isSecure = getConfiguration().getBoolean(
		// SETTING_KEY_VERIFICATION_LINK_SECURE);
		// TODO find out how to return just json
		// final String url = "";
		// final String url = routes.Signup.verify(token).absoluteURL(
		// ctx.request(), isSecure);
		String baseURL = Play.application().configuration()
				.getString("application.baseUrl");
		final String url = baseURL + routes.Signup.verify(token).url();

		final Lang lang = Lang.preferred(ctx.request().acceptLanguages());
		final String langCode = lang.code();

		String locale = langCode;

		if (user.getLocale() != null) {
			locale = user.getLocale();
		}

		if (locale.equals("it_IT") || locale.equals("it-IT")
				|| locale.equals("it")) {
			locale = "it";
		} else if (locale.equals("es_ES") || locale.equals("es-ES")
				|| locale.equals("es")) {
			locale = "es";
		} else if (locale.equals("en_EN") || locale.equals("en-EN")
				|| locale.equals("en")) {
			locale = "en";
		}

		String name = user.getPerson().getFirstname() + " "
				+ user.getPerson().getLastname();
		String email = user.getEmail();
		final String html = getEmailTemplate(
				"views.html.account.email.verify_email", locale, url, token,
				name, email);
		final String text = getEmailTemplate(
				"views.txt.account.email.verify_email", locale, url, token,
				name, email);

		return new Body(text, html);
	}

	public void sendVerifyEmailMailingAfterSignup(final User user,
			final Context ctx) {

		final String subject = getVerifyEmailMailingSubjectAfterSignup(user,
				ctx);
		final String token = generateVerificationRecord(user);
		final Body body = getVerifyEmailMailingBodyAfterSignup(token, user, ctx);
		mailer.sendMail(subject, body, getEmailName(user));
	}

	private String getEmailName(final User user) {
		return getEmailName(user.getEmail(), user.getPerson().getFirstname());
	}

	public static Result handleLogin(final Context ctx) {
		return PlayAuthenticateLocal.handleAuthentication(PROVIDER_KEY, ctx,
				getEnum("LOGIN"));
	}

	public static Result handleSignup(final Context ctx) {
		return PlayAuthenticateLocal.handleAuthentication(PROVIDER_KEY, ctx,
				getEnum("SIGNUP"));
	}

	private static Object getEnum(String enumName) {
		Object kase = null;
		try {
			Class<?> clazz = UsernamePasswordAuthProvider.class
					.getDeclaredClasses()[4];
			Field field = clazz.getDeclaredField(enumName);
			field.setAccessible(true);
			kase = field.get(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kase;
	}

}
