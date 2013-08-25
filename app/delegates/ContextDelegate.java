package delegates;

import java.util.ArrayList;
import java.util.List;

import enums.MementoCategory;
import exceptions.EntityDoesNotExist;
import exceptions.NotEnoughInformation;
import pojos.ContextBean;
import utils.PlayDozerMapper;
import models.City;
import models.Context;
import models.ContextContent;
import models.ContextContributed;
import models.ContributedMemento;
import models.LifeStory;
import models.Person;
import models.PublicMemento;

public class ContextDelegate {

	private ContextBean initContext(Long personId, Long decade, Long cityId)
			throws EntityDoesNotExist, NotEnoughInformation {
		/*
		 * 1.read person record
		 */
		Person person = Person.read(personId);

		if (person == null) {
			// person does not exist
			throw new EntityDoesNotExist("Person with ID " + personId
					+ "does not exist");
		}

		/*
		 * 2. Prepare decades and cities to iterate
		 */
		City birthplace = person.getBirthplace();
		List<LifeStory> stories = decade != null || cityId != null ? LifeStory
				.readByPerson(personId) : null;
		Long countryId = birthplace != null ? birthplace.getCountry()
				.getCountryId() : null;
		List<Long> decades = decade == null ? LifeStory.getDecades(stories)
				: new ArrayList<Long>();
		List<Long> cities = !decades.isEmpty() && cityId == null ? LifeStory
				.getCities(stories) : new ArrayList<Long>();

		if (decades.isEmpty() && decade != null) {
			decades.add(decade);
		}

		if (!decades.isEmpty() && cities.isEmpty() && cityId != null) {
			cities.add(cityId);
		} else {
			throw new NotEnoughInformation(
					"At least a decade is needed to initialize a context");
		}

		/*
		 * 3. Create the new context object
		 */
		Context newContext = new Context();
		newContext.setCityFor(birthplace);
		newContext.setPersonForId(personId);
		newContext.setCityRatio(new Long(0));
		newContext.setTitle(play.i18n.Messages.get("context.person.title")
				+ person.getFirstname() + " " + person.getLastname());
		newContext.setSubtitle(play.i18n.Messages
				.get("context.person.subtitle")
				+ person.getFirstname()
				+ " "
				+ person.getLastname());

		/*
		 * 4. Create the new lists of items (ContextMedia, ContextContributed, ContextPeople, ContextEvent, ContextWorks)
		 */

		List<ContextContributed> contributedContent = getContributedContextList(decades,cities);
//		List<ContextMedia> mediaContent = new ArrayList<ContextMedia>();
//		List<ContextEvent> eventContent = new ArrayList<ContextEvent>();
//		List<ContextCreativeWorks> creativeWorkContent = new ArrayList<ContextCreativeWorks>();
//		List<ContextPeople> peopleContent = new ArrayList<ContextPeope>();
		
		/*
		 * 6. Add the lists of  contents to the context
		 */
		newContext.setContributedMementoList(contributedContent);
		
		ContextBean newContextBean = PlayDozerMapper.getInstance().map(Context.class,ContextBean.class);
		return newContextBean;
	}

	private List<ContextContributed> getContributedContextList(
			List<Long> decades, List<Long> cities) {
		

		List<ContextContributed> contributedContent = new ArrayList<ContextContributed>();
		
		// TODO everything from here

		/*
		 * 5. Prepare content related to both the country of the person
		 */
//		for (MementoCategory category : MementoCategory.values()) { // x 4
//
//			for (Long dec : decades) { // x N
//				List<ContributedMemento> cmCities = null; 
//				for (Long city : cities) { // x M
//					cmCities = ContributedMemento.readByDecadeAndCityIdAndCategoryWithLimit(dec, city, category, 2, null);
//					for (ContextContributed publicMemento : cmCities) {
//						ContextContributed c = new ContextContributed(publicMemento);
//						contributedContent.add(c);
//					}
//					// TODO add the 2 closest items to "city" and "dec" in
//					// "category" from the contributed index of knowledge
//				}
//
//				List<PublicMemento> pmCountry = PublicMemento.readByDecadeAndCountryIdAndCategoryWithLimit(dec, countryId, category, 2, cmCities);
//				
//				for (PublicMemento publicMemento : pmCountry) {
//					ContextContent c = new ContextContent(publicMemento);
//					contributedContent.add(c);
//				}
//
//				cmCities.addAll(pmCountry);
//				
//				// TODO add the 2 closest items to "city" and "dec" in
//				// "category" from the contributed index of knowledge
//				
//
//				List<PublicMemento> pm = PublicMemento.readByDecadeAndCategoryWithLimit(dec, category, 2, cmCities);
//				for (PublicMemento publicMemento : pm) {
//					ContextContent c = new ContextContent(publicMemento);
//					content.add(c);
//				}
//				
//				// TODO add the 2 random item within and "dec" in
//				// "category" from the contributed index of knowledge
//			}
//		}
		return null;
	}

	public static ContextDelegate getInstance() {
		return new ContextDelegate();
	}

	public ContextBean initContextForPersonAndDecadeAndCity(Long personId,
			Long decade, Long cityId) throws EntityDoesNotExist,
			NotEnoughInformation {
		return initContext(personId, decade, cityId);
	}

	public ContextBean initContextForPersonAndDecade(Long personId, Long decade)
			throws EntityDoesNotExist, NotEnoughInformation {
		return initContext(personId, decade, null);
	}

	public ContextBean initContextForPerson(Long personId)
			throws EntityDoesNotExist, NotEnoughInformation {
		return initContext(personId, null, null);
	}
}
