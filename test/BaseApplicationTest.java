import java.io.IOException;

import org.junit.*;
import play.test.FakeApplication;
import play.test.Helpers;
//import play.db.jpa.JPA;
//import play.db.jpa.JPAPlugin;
//import scala.Option;
//import javax.persistence.EntityManager;

public class BaseApplicationTest {
	public static FakeApplication app;
//	public static String createDdl = "";
//	public static String dropDdl = "";
//	private static EntityManager em;

	@BeforeClass
	public static void startApp() throws IOException {
//		app = Helpers.fakeApplication();		
		app = Helpers.fakeApplication(Helpers.inMemoryDatabase("default-test"));	
		Helpers.start(app);		
//		Trying to make JPA.em(), but didn't work. 
//		Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
//		em = jpaPlugin.get().em("default");
//	    JPA.bindForCurrentThread(em);
	}

	@AfterClass
	public static void stopApp() {
		Helpers.stop(app);
//		Trying to make JPA.em(), but didn't work. 
//		JPA.bindForCurrentThread(null);
//		em.close();
	}
}