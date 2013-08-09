package delegates;

import java.util.ArrayList;
import java.util.List;

import models.LifeStory;
import models.Participation;
import models.Person;

import pojos.LifeStoryBean;
import utils.PlayDozerMapper;

public class LifeStoryDelegate {

	public static LifeStoryDelegate getInstance() {
		return new LifeStoryDelegate();
	}

	public List<LifeStoryBean> getAll() {
		List<models.LifeStory> modelLifeStorys = models.LifeStory.all();
		List<LifeStoryBean> pojosLifeStorys = new ArrayList<LifeStoryBean>();
		for (models.LifeStory lifeStory : modelLifeStorys) {
			LifeStoryBean lifeStoryBean = PlayDozerMapper.getInstance().map(
					lifeStory, LifeStoryBean.class);
			pojosLifeStorys.add(lifeStoryBean);
		}
		return pojosLifeStorys;
	}

	public List<LifeStoryBean> getAllByPersonId(Long personId) {
		List<models.LifeStory> modelLifeStorys = models.LifeStory
				.readByPersonIncludingNonProtagonist(personId);
		List<LifeStoryBean> pojosLifeStorys = new ArrayList<LifeStoryBean>();
		for (models.LifeStory lifeStory : modelLifeStorys) {
			LifeStoryBean lifeStoryBean = PlayDozerMapper.getInstance().map(
					lifeStory, LifeStoryBean.class);
			pojosLifeStorys.add(lifeStoryBean);
		}
		return pojosLifeStorys;
	}

	public List<LifeStoryBean> getAllByProtagonistId(Long personId) {
		List<models.LifeStory> modelLifeStorys = models.LifeStory
				.readByPerson(personId);
		List<LifeStoryBean> pojosLifeStorys = new ArrayList<LifeStoryBean>();
		for (models.LifeStory lifeStory : modelLifeStorys) {
			LifeStoryBean lifeStoryBean = PlayDozerMapper.getInstance().map(
					lifeStory, LifeStoryBean.class);
			pojosLifeStorys.add(lifeStoryBean);
		}
		return pojosLifeStorys;
	}

	public LifeStoryBean getLifeStory(Long id) {
		models.LifeStory lifeStory = models.LifeStory.read(id);
		if (lifeStory != null) {
			LifeStoryBean lifeStoryBean = PlayDozerMapper.getInstance().map(
					lifeStory, LifeStoryBean.class);
			return lifeStoryBean;
		} else {
			return null;
		}
	}

	public LifeStoryBean create(LifeStoryBean lifeStoryBean) {
		models.LifeStory lifeStory = PlayDozerMapper.getInstance().map(
				lifeStoryBean, models.LifeStory.class);
		models.LifeStory.create(lifeStory);
		lifeStory = models.LifeStory.read(lifeStory.getLifeStoryId());
		LifeStoryBean newStory = new LifeStoryBean();
		PlayDozerMapper.getInstance().map(lifeStory, newStory);
		return newStory;
	}

	public LifeStoryBean update(LifeStoryBean bean, Long id) {
		models.LifeStory lifeStory = PlayDozerMapper.getInstance().map(bean,
				models.LifeStory.class);
		lifeStory.update(id);
		lifeStory = models.LifeStory.read(lifeStory.getLifeStoryId());
		LifeStoryBean updatedStory = new LifeStoryBean();
		PlayDozerMapper.getInstance().map(lifeStory, updatedStory);
		return updatedStory;
	}

	public void deleteLifeStory(Long id) {
		models.LifeStory.delete(id);
	}

	public Long addParticipant(Long lsid, Long personId, Long contributor, Boolean isProtagonist) {
		LifeStory story = LifeStory.read(lsid);
		Person person = Person.read(personId);
		
		Participation part = new Participation();
		part.setContributorId(contributor);
		part.setLifeStory(story);
		part.setPerson(person);
		part.setProtagonist(isProtagonist);
		
		story.addParticipant(part);
		
		return part.getParticipationId();
	}

	public void deleteParticipant(Long lsid, Long id) throws Exception {
		models.LifeStory lifeStory = models.LifeStory.read(lsid);		
		lifeStory.deleteParticipant(id);
		lifeStory.update();	
	}

	public void deleteProtagonist(Long lsid, Long id) {
		models.LifeStory lifeStory = models.LifeStory.read(lsid);		
		lifeStory.deleteProtagonist(id);
		lifeStory.update();	
	}
	
	
}
