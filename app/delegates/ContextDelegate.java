package delegates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import enums.MementoCategory;
import exceptions.EntityDoesNotExist;
import exceptions.NotEnoughInformation;
import pojos.ContextBean;
import pojos.ContextContributedBean;
import pojos.ContributedMementoBean;
import pojos.LifeStoryBean;
import pojos.LocationMinimalBean;
import utils.PlayDozerMapper;
import models.City;
import models.Context;
import models.ContextContributed;
import models.ContextCreativeWork;
import models.ContextEvent;
import models.ContextMedia;
import models.ContextPeople;
import models.ContributedMemento;
import models.CreativeWork;
import models.Event;
import models.FamousPerson;
import models.FuzzyDate;
import models.LifeStory;
import models.Location;
import models.Media;
import models.Person;
import models.User;
import play.Play;

public class ContextDelegate {

	public static ContextDelegate getInstance() {
		return new ContextDelegate();
	}

	public ContextBean getContextForPerson(Long personId) {
		models.Context context = models.Context.findByPerson(personId);
		if (context != null) {
			ContextBean contextBean = PlayDozerMapper.getInstance().map(
					context, ContextBean.class);
			return contextBean;
		} else {
			return null;
		}
	}

	public ContextBean initContextForPerson(Long personId)
			throws EntityDoesNotExist, NotEnoughInformation {
		return initContext(personId, null, null, null);
	}

	public ContextBean getContextForPersonAndDecade(Long id, Long decade) {
		models.Context context = models.Context.findByPersonAndDecade(id,decade);
		if (context != null) {
			ContextBean contextBean = PlayDozerMapper.getInstance().map(
					context, ContextBean.class);
			return contextBean;
		} else {
			return null;
		}
	}
	
	public ContextBean getContextForPersonAndDecadeAndCategory(Long id, Long decade, String category) {
		models.Context context = models.Context.findByPersonAndDecadeAndCategory(id,decade,category);
		if (context != null) {
			ContextBean contextBean = PlayDozerMapper.getInstance().map(
					context, ContextBean.class);
			return contextBean;
		} else {
			return null;
		}
	}
		
	public ContextBean initContextForPersonAndDecade(Long personId, Long decade)
			throws EntityDoesNotExist, NotEnoughInformation {
		return initContext(personId, decade, null, null);
	}

	public ContextBean initContextForPersonAndDecadeAndLocation(Long personId,
			Long decade, LocationMinimalBean location)
			throws EntityDoesNotExist, NotEnoughInformation {
		return initContext(personId, decade, null, location);
	}

	public ContextBean initContextForPersonAndDecadeAndCity(Long personId,
			Long decade, Long cityId) throws EntityDoesNotExist,
			NotEnoughInformation {
		return initContext(personId, decade, cityId, null);
	}
	
	public ContextBean getContext(Long contextId) {
		models.Context context = models.Context.read(contextId);
		if (context != null) {
			ContextBean contextBean = PlayDozerMapper.getInstance().map(
					context, ContextBean.class);
			return contextBean;
		} else {
			return null;
		}
	}
	
	public ContextBean getContextByIdAndDecade(Long contextId, Long decade) {
		models.Context context = models.Context.findByIdAndDecade(contextId,decade);
		if (context != null) {
			ContextBean contextBean = PlayDozerMapper.getInstance().map(
					context, ContextBean.class);
			return contextBean;
		} else {
			return null;
		}
	}
	
	public ContextBean getContextByIdAndDecadeAndCategory(Long contextId, Long decade, String category) {
		models.Context context = models.Context.findByIdAndDecadeAndCategory(contextId, decade, category);
		if (context != null) {
			ContextBean contextBean = PlayDozerMapper.getInstance().map(
					context, ContextBean.class);
			return contextBean;
		} else {
			return null;
		}
	}
	
	public ContextBean refreshContextAndDecadeAndLocation(Long id,
			Long decade, LocationMinimalBean location)
			throws EntityDoesNotExist, NotEnoughInformation {
		// TODO Auto-generated method stub
		return null;
	}


	public ContextBean refreshContextForDecadeAndCity(Long cid, Long decade,
			Long cityId) throws EntityDoesNotExist, NotEnoughInformation {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteContext(Long cid) {
		models.Context.delete(cid);
	}

	public ContextContributedBean addMementoToContext(Long cid,
			ContributedMementoBean contributed) throws EntityDoesNotExist,
			NotEnoughInformation {
		models.ContributedMemento contributedMemento = PlayDozerMapper.getInstance().map(
				contributed, models.ContributedMemento.class);
		models.ContributedMemento.create(contributedMemento);		
		models.Context c = models.Context.read(cid);
		models.ContextContributed cc = new ContextContributed(contributedMemento, c);
		//models.ContextContributed.create(cc);
		c.getContributedMementoList().add(cc);
		models.Context.update(cid);
		cc.refresh();
		return PlayDozerMapper.getInstance().map(cc, pojos.ContextContributedBean.class);
	}

	public ContributedMementoBean updatedContextMemento(Long cid, Long mid,
			ContributedMementoBean contributed) throws EntityDoesNotExist,
			NotEnoughInformation {
		models.ContributedMemento contributedMemento = PlayDozerMapper.getInstance().map(contributed, models.ContributedMemento.class);
		contributedMemento.update();
		return PlayDozerMapper.getInstance().map(contributedMemento, pojos.ContributedMementoBean.class);
	}

	public void deleteContextMemento(Long cid, Long mid)
			throws EntityDoesNotExist, NotEnoughInformation {
		models.ContextContributed.delete(cid, mid);
	}

	public ContributedMemento createContributedMemento(
			ContributedMementoBean contributed) {
		models.ContributedMemento contributedMemento = PlayDozerMapper.getInstance().map(
				contributed, models.ContributedMemento.class);
		models.ContributedMemento.create(contributedMemento);		
		return contributedMemento;
	}

	/*
	 * 
	 * The algorithm methods
	 * 
	 * TODO unify into one single model
	 */
	
	/**
	 * The initContext method creates a whole new collection of contextual reminiscence items for 
	 * the person identified by personId. 
	 * 
	 * The initialization process will focus on the provided decade, city and location (or combination of them)
	 * If none is provided, it will use the list of stories of the person that are present in the system. 
	 * 
	 * @param personId
	 * @param decade
	 * @param cityId
	 * @param location
	 * @return
	 * @throws EntityDoesNotExist
	 * @throws NotEnoughInformation
	 */
	private ContextBean initContext(Long personId, Long decade, Long cityId,
			LocationMinimalBean location) throws EntityDoesNotExist,
			NotEnoughInformation {
		/*
		 * 1.read person record and language information
		 */
		Person person = Person.read(personId);
		String locale = User.readLocaleByPersonId(personId);

		if (person == null) {
			// person does not exist
			throw new EntityDoesNotExist("Person with ID " + personId
					+ "does not exist");
		}

		/*
		 * 2. Prepare decades
		 */
		Map<Long, List<LocationMinimalBean>> decadesLocationsMap = prepareDecadesLocationsMap(
				decade, cityId, personId, location, locale);

		if (decadesLocationsMap.isEmpty()) {
			throw new NotEnoughInformation("The person " + personId
					+ "has no story in this decade");
		}

		/*
		 * 3. Disable old context for person
		 */
		Context.disableContextsForPerson(personId);

		/*
		 * 4. Create the new context object
		 */
		Context newContext = new Context();
		newContext.setCityFor(null);
		newContext.setPersonForId(personId);
		newContext.setCityRatio(null);
		newContext.setTitle(play.i18n.Messages
				.get("reminiscens.context.person.title")
				+ " "
				+ person.getFirstname() + " " + person.getLastname());
		newContext.setSubtitle(play.i18n.Messages
				.get("reminiscens.context.person.subtitle")
				+ " "
				+ person.getFirstname() + " " + person.getLastname());
		newContext.setEnabled(true);

		// save the newly created context
		Context.createObject(newContext);

		/*
		 * 4 For each decade, prepare content to put in context creating new
		 * lists of items (ContextMedia, ContextContributed, ContextPeople,
		 * ContextEvent, ContextWorks) for the locations where the person had a
		 * story in those decades
		 */

		List<ContextContributed> contributedContent = getContributedContextList(
				decadesLocationsMap, locale, newContext);
		List<ContextMedia> mediaContent = getMediaContextList(
				decadesLocationsMap, locale, newContext);
		List<ContextEvent> eventContent = getEventContextList(
				decadesLocationsMap, locale, newContext);
		List<ContextCreativeWork> creativeWorkContent = getCreativeWorkContextList(
				decadesLocationsMap, locale, newContext);
		List<ContextPeople> peopleContent = getPeopleContextList(
				decadesLocationsMap, locale, newContext);

		/*
		 * 5. Add the lists of contents to the context
		 */
		newContext.setContributedMementoList(contributedContent);
		newContext.setMediaList(mediaContent);
		newContext.setEventList(eventContent);
		newContext.setCreativeWorkList(creativeWorkContent);
		newContext.setFamousPeopleList(peopleContent);

		// newContext.update();
		// newContext.refresh();

		ContextBean newContextBean = PlayDozerMapper.getInstance().map(
				newContext, ContextBean.class);
		return newContextBean;
	}
	
	private List<ContextPeople> getPeopleContextList(
			Map<Long, List<LocationMinimalBean>> decadesLocationsMap,
			String locale, Context newContext) {
		Set<Long> decades = decadesLocationsMap.keySet();
		List<ContextPeople> content = new ArrayList<ContextPeople>();
		int itemsPerLevel = Play.application().configuration()
				.getInt("context.algorithm.item.count");
		if (itemsPerLevel <= 0) {
			itemsPerLevel = 1;
		}

		for (Long decade : decades) {
			for (MementoCategory category : MementoCategory.values()) {
				// 1. read 1 random element related only to the decade
				List<LocationMinimalBean> locations = decadesLocationsMap
						.get(decade);
				List<FamousPerson> worldLevelList = FamousPerson
						.readForContext(locale, category, decade, locations,
								"WORLD", itemsPerLevel);
				List<FamousPerson> countryLevelList = FamousPerson
						.readForContext(locale, category, decade, locations,
								"COUNTRY", itemsPerLevel);
				List<FamousPerson> regionLevelList = FamousPerson
						.readForContext(locale, category, decade, locations,
								"REGION", itemsPerLevel);

				for (FamousPerson contributed : worldLevelList) {
					ContextPeople contextItem = new ContextPeople(
							contributed, newContext);
					contextItem.setLevel("WORLD");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextPeople.create(contextItem);
					content.add(contextItem);

				}
				for (FamousPerson contributed : countryLevelList) {
					ContextPeople contextItem = new ContextPeople(
							contributed, newContext);
					contextItem.setLevel("COUNTRY");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextPeople.create(contextItem);
					content.add(contextItem);
				}
				for (FamousPerson contributed : regionLevelList) {
					ContextPeople contextItem = new ContextPeople(
							contributed, newContext);
					contextItem.setLevel("REGION");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextPeople.create(contextItem);
					content.add(contextItem);
				}
			}
		}
		return content;
	}

	
	
	private List<ContextCreativeWork> getCreativeWorkContextList(
			Map<Long, List<LocationMinimalBean>> decadesLocationsMap,
			String locale, Context newContext) {
		Set<Long> decades = decadesLocationsMap.keySet();
		List<ContextCreativeWork> content = new ArrayList<ContextCreativeWork>();
		int itemsPerLevel = Play.application().configuration()
				.getInt("context.algorithm.item.count");
		if (itemsPerLevel <= 0) {
			itemsPerLevel = 1;
		}

		for (Long decade : decades) {
			for (MementoCategory category : MementoCategory.values()) {
				// 1. read 1 random element related only to the decade
				List<LocationMinimalBean> locations = decadesLocationsMap
						.get(decade);
				List<CreativeWork> worldLevelList = CreativeWork
						.readForContext(locale, category, decade, locations,
								"WORLD", itemsPerLevel);
				List<CreativeWork> countryLevelList = CreativeWork
						.readForContext(locale, category, decade, locations,
								"COUNTRY", itemsPerLevel);
				List<CreativeWork> regionLevelList = CreativeWork
						.readForContext(locale, category, decade, locations,
								"REGION", itemsPerLevel);

				for (CreativeWork contributed : worldLevelList) {
					ContextCreativeWork contextItem = new ContextCreativeWork(
							contributed, newContext);
					contextItem.setLevel("WORLD");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextCreativeWork.create(contextItem);
					content.add(contextItem);

				}
				for (CreativeWork contributed : countryLevelList) {
					ContextCreativeWork contextItem = new ContextCreativeWork(
							contributed, newContext);
					contextItem.setLevel("COUNTRY");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextCreativeWork.create(contextItem);
					content.add(contextItem);
				}
				for (CreativeWork contributed : regionLevelList) {
					ContextCreativeWork contextItem = new ContextCreativeWork(
							contributed, newContext);
					contextItem.setLevel("REGION");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextCreativeWork.create(contextItem);
					content.add(contextItem);
				}
			}
		}
		return content;
	}

	private List<ContextEvent> getEventContextList(
			Map<Long, List<LocationMinimalBean>> decadesLocationsMap,
			String locale, Context newContext) {
		Set<Long> decades = decadesLocationsMap.keySet();
		List<ContextEvent> content = new ArrayList<ContextEvent>();
		int itemsPerLevel = Play.application().configuration()
				.getInt("context.algorithm.item.count");
		if (itemsPerLevel <= 0) {
			itemsPerLevel = 1;
		}

		String category = "STORY";
		for (Long decade : decades) {
			// 1. read 1 random element related only to the decade
			List<LocationMinimalBean> locations = decadesLocationsMap
					.get(decade);
			List<Event> worldLevelList = Event.readForContext(locale, category,
					decade, locations, "WORLD", itemsPerLevel);
			List<Event> countryLevelList = Event.readForContext(locale,
					category, decade, locations, "COUNTRY", itemsPerLevel);
			List<Event> regionLevelList = Event.readForContext(locale,
					category, decade, locations, "REGION", itemsPerLevel);

			for (Event contributed : worldLevelList) {
				ContextEvent contextItem = new ContextEvent(contributed,
						newContext);
				contextItem.setLevel("WORLD");
				contextItem.setDecade(decade);
				contextItem.setCategory(contributed.getCategory());
				contextItem.setType(contributed.getResourceType());
				ContextEvent.create(contextItem);
				content.add(contextItem);

			}
			for (Event contributed : countryLevelList) {
				ContextEvent contextItem = new ContextEvent(contributed,
						newContext);
				contextItem.setLevel("COUNTRY");
				contextItem.setDecade(decade);
				contextItem.setCategory(contributed.getCategory());
				contextItem.setType(contributed.getResourceType());
				ContextEvent.create(contextItem);
				content.add(contextItem);
			}
			for (Event contributed : regionLevelList) {
				ContextEvent contextItem = new ContextEvent(contributed,
						newContext);
				contextItem.setLevel("REGION");
				contextItem.setDecade(decade);
				contextItem.setCategory(contributed.getCategory());
				contextItem.setType(contributed.getResourceType());
				ContextEvent.create(contextItem);
				content.add(contextItem);
			}
		}

		return content;
	}

	private List<ContextMedia> getMediaContextList(
			Map<Long, List<LocationMinimalBean>> decadesLocationsMap,
			String locale, Context newContext) {

		Set<Long> decades = decadesLocationsMap.keySet();
		List<ContextMedia> content = new ArrayList<ContextMedia>();
		int itemsPerLevel = Play.application().configuration()
				.getInt("context.algorithm.item.count");
		if (itemsPerLevel <= 0) {
			itemsPerLevel = 1;
		}

		for (Long decade : decades) {
			for (MementoCategory category : MementoCategory.values()) {
				// 1. read 1 random element related only to the decade
				List<LocationMinimalBean> locations = decadesLocationsMap
						.get(decade);
				List<Media> worldLevelList = Media.readForContext(locale,
						category, decade, locations, "WORLD", itemsPerLevel);
				List<Media> countryLevelList = Media.readForContext(locale,
						category, decade, locations, "COUNTRY", itemsPerLevel);
				List<Media> regionLevelList = Media.readForContext(locale,
						category, decade, locations, "REGION", itemsPerLevel);

				for (Media contributed : worldLevelList) {
					ContextMedia contextItem = new ContextMedia(contributed,
							newContext);
					contextItem.setLevel("WORLD");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextMedia.create(contextItem);
					content.add(contextItem);

				}
				for (Media contributed : countryLevelList) {
					ContextMedia contextItem = new ContextMedia(contributed,
							newContext);
					contextItem.setLevel("COUNTRY");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextMedia.create(contextItem);
					content.add(contextItem);
				}
				for (Media contributed : regionLevelList) {
					ContextMedia contextItem = new ContextMedia(contributed,
							newContext);
					contextItem.setLevel("REGION");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextMedia.create(contextItem);
					content.add(contextItem);
				}
			}
		}

		return content;
	}

	private List<ContextContributed> getContributedContextList(
			Map<Long, List<LocationMinimalBean>> decadesLocationsMap,
			String locale, Context newContext) {
		Set<Long> decades = decadesLocationsMap.keySet();
		List<ContextContributed> content = new ArrayList<ContextContributed>();
		int itemsPerLevel = Play.application().configuration()
				.getInt("context.algorithm.item.count");
		if (itemsPerLevel <= 0) {
			itemsPerLevel = 1;
		}

		for (Long decade : decades) {
			for (MementoCategory category : MementoCategory.values()) {
				// 1. read 1 random element related only to the decade
				List<LocationMinimalBean> locations = decadesLocationsMap
						.get(decade);
				List<ContributedMemento> worldLevelList = ContributedMemento
						.readForContext(locale, category, decade, locations,
								"WORLD", itemsPerLevel);
				List<ContributedMemento> countryLevelList = ContributedMemento
						.readForContext(locale, category, decade, locations,
								"COUNTRY", itemsPerLevel);
				List<ContributedMemento> regionLevelList = ContributedMemento
						.readForContext(locale, category, decade, locations,
								"REGION", itemsPerLevel);

				for (ContributedMemento contributed : worldLevelList) {
					ContextContributed contextItem = new ContextContributed(
							contributed, newContext);
					contextItem.setLevel("WORLD");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextContributed.create(contextItem);
					content.add(contextItem);

				}
				for (ContributedMemento contributed : countryLevelList) {
					ContextContributed contextItem = new ContextContributed(
							contributed, newContext);
					contextItem.setLevel("COUNTRY");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextContributed.create(contextItem);
					content.add(contextItem);
				}
				for (ContributedMemento contributed : regionLevelList) {
					ContextContributed contextItem = new ContextContributed(
							contributed, newContext);
					contextItem.setLevel("REGION");
					contextItem.setDecade(decade);
					contextItem.setCategory(contributed.getCategory());
					contextItem.setType(contributed.getResourceType());
					ContextContributed.create(contextItem);
					content.add(contextItem);
				}
			}
		}

		return content;
	}


	/**
	 * Prepare a HastTable of the form decade => list of locations which will be
	 * used to iterate and generate the context
	 * 
	 * @param decade
	 * @param cityId
	 * @param personId
	 * @param location
	 * @return
	 */	
	private Map<Long, List<LocationMinimalBean>> prepareDecadesLocationsMap(
			Long decade, Long cityId, Long personId,
			LocationMinimalBean location, String locale) {
		// TODO replace with reading directly the distinct decades and locations
		List<LifeStory> stories = cityId == null ? location == null ? LifeStory
				.readByPerson(personId) : null : null;

		Map<Long, List<LocationMinimalBean>> map = new HashMap<Long, List<LocationMinimalBean>>();
		if (stories != null && !stories.isEmpty() && decade != null) {
			map = storiesToDecadeLocationsMap(stories, decade);
		} else if (stories != null && !stories.isEmpty() && decade == null) {
			map = storiesToDecadeLocationsMap(stories);
		} else if (decade != null) {
			List<LocationMinimalBean> locList = new ArrayList<LocationMinimalBean>();

			if (cityId != null) {
				City c = City.read(cityId);
				LocationMinimalBean loc = new LocationMinimalBean(c
						.getCountry().getNameByLocale(locale), c.getRegion(),
						c.getName(), locale);
				locList.add(loc);
				map.put(decade, locList);
			} else if (location != null) {
				locList.add(location);
				map.put(decade, locList);
			}
		}
		return map;
	}

	private Map<Long, List<LocationMinimalBean>> storiesToDecadeLocationsMap(
			List<LifeStory> stories, Long decade) {
		Map<Long, List<LocationMinimalBean>> map = new HashMap<Long, List<LocationMinimalBean>>();
		if (stories != null) {
			for (LifeStory lifeStory : stories) {
				FuzzyDate d = lifeStory.getStartDate();
				Long localDecade = d != null ? d.getDecade() : null;
				if (localDecade.equals(decade)) {
					Location c = lifeStory.getLocation();
					String locale = c.getLocale() == null
							|| c.getLocale() == "" ? Play.application()
							.configuration().getString("default.language") : c
							.getLocale();
					LocationMinimalBean loc = new LocationMinimalBean(
							c.getCountry(), c.getRegion(), c.getCity(),
							locale);
					List<LocationMinimalBean> locList = map.get(decade);
					if (locList == null) {
						locList = new ArrayList<LocationMinimalBean>();
					}
					if (!locList.contains(loc)) {
						locList.add(loc);
					}
					map.put(decade, locList);
				}
			}
		}
		return map;
	}

	private Map<Long, List<LocationMinimalBean>> storiesToDecadeLocationsMap(
			List<LifeStory> stories) {
		Map<Long, List<LocationMinimalBean>> map = new HashMap<Long, List<LocationMinimalBean>>();
		if (stories != null) {
			for (LifeStory lifeStory : stories) {
				FuzzyDate d = lifeStory.getStartDate();
				Long decade = d != null ? d.getDecade() : null;
				if (decade != null) {
					Location c = lifeStory.getLocation();
					String locale = c.getLocale() == null
							|| c.getLocale() == "" ? Play.application()
							.configuration().getString("default.language") : c
							.getLocale();

					// TODO manage localization of locations
					// String localizedCountry =
					// Country.readByName(c.getCountry()).getNameByLocale(locale);
					// String localizedCity = Country

					LocationMinimalBean loc = new LocationMinimalBean(
							c.getCountry(), c.getRegion(), c.getCity(),
							locale);
					List<LocationMinimalBean> locList = map.get(decade);
					if (locList == null) {
						locList = new ArrayList<LocationMinimalBean>();
					}
					if (!locList.contains(loc)) {
						locList.add(loc);
					}
					map.put(decade, locList);
				}
			}
		}
		return map;
	}
}
