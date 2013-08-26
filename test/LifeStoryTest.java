

import java.util.List;

import static org.fest.assertions.Assertions.*;
import org.junit.Test;


import play.Logger;
import models.LifeStory;
import models.Location;
import models.Person;


/**
 * 
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 * 
 */
public class LifeStoryTest extends BaseApplicationTest {

	@Test
	public void getDecadesTest() {
		List<Person> pList = Person.all();
		assertThat(pList).isNotNull();
		assertThat(pList).isNotEmpty();
		
		Person p = pList.get(4);
		Long personId = p.getPersonId();
		Logger.debug("# TESTING LifeStory Model. Using Person = " + personId);
		
		List<LifeStory> s = LifeStory.readByPerson(personId);
		assertThat(s).isNotEmpty();
		Logger.debug("# TESTING Person stories = " + s.size());

		List<Long> decades = LifeStory.getDecades(s);
		Logger.debug("# TESTING Stories decades = " + decades.toString());
		assertThat(decades).isNotEmpty();
		assertThat(decades).contains(new Long(1970));
	}
	
	@Test
	public void getLocationsByDecadeTest() {
		List<Person> pList = Person.all();
		assertThat(pList).isNotNull();
		assertThat(pList).isNotEmpty();
		
		Person p = pList.get(4);
		Long personId = p.getPersonId();
		Logger.debug("# TESTING LifeStory Model. Using Person = " + personId);
		
		List<LifeStory> s = LifeStory.readByPerson(personId);
		assertThat(s).isNotEmpty();
		Logger.debug("# TESTING Person stories = " + s.size());

		List<Location> locations = LifeStory.getLocationsByDecade(s, new Long(1960));
		Logger.debug("# TESTING Stories locations = " + locations.toString());
		assertThat(locations).isNotEmpty();
	}
	
//	Find a way to execute tests that use models that use the JPA entity manager	
//	@Test
//	@Transactional
//	public void getDecadesEMTest() {
//		List<Person> pList = Person.all();
//		assertThat(pList).isNotNull();
//		assertThat(pList).isNotEmpty();
//		
//		Person p = pList.get(4);
//		Long personId = p.getPersonId();
//		Logger.debug("# TESTING Using Person = " + personId);
//
//		List<Long> decades = LifeStory.getDecadesByPerson(personId);
//		Logger.debug("# TESTING Person stories decades = " + decades.toString());
//		assertThat(decades).isNotEmpty();
//		assertThat(decades).contains(new Long(1970));
//	}
}
