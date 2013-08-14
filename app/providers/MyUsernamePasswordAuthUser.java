package providers;

import pojos.PersonBean;
import providers.MyUsernamePasswordAuthProvider.MySignup;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.NameIdentity;
import com.feth.play.module.pa.user.PicturedIdentity;

public class MyUsernamePasswordAuthUser extends UsernamePasswordAuthUser
		implements NameIdentity, PicturedIdentity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private final PersonBean person;
	private Long userId;
	private final String profilePic;


	public MyUsernamePasswordAuthUser(final MySignup signup) {
		super(signup.password, signup.email);
		this.name = signup.name;
		this.person = signup.person;
		this.userId = signup.userId;
		this.profilePic = signup.profilePic;
	}
	
	/**
	 * just for testing purpose, should not be used for the app itself
	 * @param name
	 * @param email
	 * @param password
	 */
	@Deprecated
	public MyUsernamePasswordAuthUser(final String name, PersonBean person, final Long userId, final String email, final String password, String profilePic) {
		super(password, email);
		this.name = name;
		this.person = person;
		this.userId = userId;
		this.profilePic = profilePic;
	}

	/**
	 * Used for password reset only - do not use this to signup a user!
	 * @param password
	 */
	public MyUsernamePasswordAuthUser(final String password) {
		super(password, null);
		this.name = null;
		this.person=null;
		this.userId = null;
		this.profilePic = null;
	}

	@Override
	public String getName() {
		return name;
	}

	public PersonBean getPerson() {
		return person;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId (Long userId) {
		this.userId=userId;
	}

	@Override
	public String getPicture() {
		return profilePic;
	}
	
//	@Override
//	public String getId(){
//		super.
//		return super.getId();
//	}
}
