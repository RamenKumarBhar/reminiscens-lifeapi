import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import models.Context;
import models.Person;

import org.junit.*;

import play.Logger;
import pojos.ContextBean;

import delegates.ContextDelegate;
import exceptions.EntityDoesNotExist;
import exceptions.NotEnoughInformation;

public class ContextDelegateTest extends BaseApplicationTest {

//	@BeforeClass
//	public void loadDozerMappings() {
//		// Read DozerMapping xml configurations file
//		InputStream stream = BaseApplicationTest.class
//				.getResourceAsStream("conf/dozerBeanMapping.xml");
//		System.out.println(stream != null);
//		stream = Test.class.getClassLoader().getResourceAsStream(
//				"conf/dozerBeanMapping.xml");
//		System.out.println(stream != null);
//	}

	@Test
	public void initContextTest() throws EntityDoesNotExist,
			NotEnoughInformation {
		// Read DozerMapping xml configurations file
//		InputStream stream = ContextDelegateTest.class
//				.getResourceAsStream("conf/dozerBeanMapping.xml");
//		System.out.println(stream != null);
//		stream = Test.class.getClassLoader().getResourceAsStream(
//				"conf/dozerBeanMapping.xml");
//		System.out.println(stream != null);
		
		List<Person> pList = Person.all();
		assertThat(pList).isNotNull();
		assertThat(pList).isNotEmpty();

		Person p = pList.get(4);
		Long personId = p.getPersonId();
		String fullname = p.getFirstname() + " " + p.getLastname();
		Logger.debug("# TESTING ContextDelegate initialization of context with Person = "
				+ fullname + " (" + personId + ")");

		ContextBean cb = ContextDelegate.getInstance().initContextForPerson(personId);
		int contributedSize = cb.getContributedMementoList().size();
		
		Context c = Context.findByPerson(personId);
		assertThat(c).isNotNull();
		assertThat(c.getContributedMementoList()).isNotEmpty();
		int contributedSizeStored = c.getContributedMementoList().size();
		assertThat(contributedSize).isEqualTo(contributedSizeStored);
	}

	@Test
	public void refreshContextTest() {

	}
}