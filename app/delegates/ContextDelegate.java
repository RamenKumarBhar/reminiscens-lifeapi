package delegates;

import java.util.ArrayList;
import java.util.List;

import enums.MementoCategory;
import enums.ResponseStatus;
import exceptions.EntityDoesNotExist;
import pojos.ContextBean;
import scala.Array;
import models.City;
import models.LifeStory;
import models.Person;

public class ContextDelegate {
	
	private ContextBean initContext(Long personId, Long decade, Long cityId) throws EntityDoesNotExist {
		/*
		 * 1.read person record
		 */
		Person person = Person.read(personId);
		
		if (person == null) {
			// person does not exist
			throw new EntityDoesNotExist("Person with ID "+personId+"does not exist");
		}
		
		/*
		 * 2. Prepare decades and cities to iterate
		 */
		City birthplace = person.getBirthplace();
		List<LifeStory> stories = LifeStory.readByPerson(personId);
		
		Long countryId =  birthplace != null ? birthplace.getCountry().getCountryId() : null;
		List<Long> decades = decade == null ? LifeStory.getDecades(stories) : new ArrayList<Long>();
		List<Long> cities = cityId == null ? LifeStory.getCities(stories) : new ArrayList<Long>();
		
		if (decades.isEmpty() && decade != null) {
			decades.add(decade);
		}

		if (cities.isEmpty() && cityId != null) {
			cities.add(cityId);
		}
		

		/*
		 * 3. Create the context
		 */
		
		// TODO everything from here
		
		
		
		return null;
	}
		
	public static ContextDelegate getInstance() {
		return new ContextDelegate();
	}

	public ContextBean initContextForPersonAndDecadeAndCity(Long personId, Long decade, Long cityId) throws EntityDoesNotExist { 
		return initContext(personId, decade, cityId);
	}

	public ContextBean initContextForPersonAndDecade(Long personId, Long decade) throws EntityDoesNotExist {
		return initContext(personId, decade, null);
	}

	public ContextBean initContextForPerson(Long personId) throws EntityDoesNotExist {
		return initContext(personId, null, null);
	}
}
