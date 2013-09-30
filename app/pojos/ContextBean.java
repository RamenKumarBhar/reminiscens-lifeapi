package pojos;


import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import utils.JodaDateTime;

public class ContextBean  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3980159110846013300L;

    private Long contextId;
	private String title;
	private String subtitle;
	private Long personForId;
	private CityBean cityFor;
	private Long cityRatio;
	@JodaDateTime(format = "yyyy-MM-dd HH:mm:ss")
	private DateTime date;
	private Long dateRatio;
	private List<ContextPublicMementoBean> publicMementoList;
	private String hashcode;
//	private List<ContextContentBean> publicContextContent;
//	private List<ContextContributedBean> contributedMementoList;
//	private List<ContextMediaBean> mediaList;
//	private List<ContextEventBean> eventList;
//	private List<ContextCreativeWorkBean> creativeWorkList;
//	private List<ContextPeopleBean> famousPeopleList;
	public Long getContextId() {
		return contextId;
	}
	public void setContextId(Long contextId) {
		this.contextId = contextId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public Long getPersonForId() {
		return personForId;
	}
	public void setPersonForId(Long personForId) {
		this.personForId = personForId;
	}
	public CityBean getCityFor() {
		return cityFor;
	}
	public void setCityFor(CityBean cityFor) {
		this.cityFor = cityFor;
	}
	public Long getCityRatio() {
		return cityRatio;
	}
	public void setCityRatio(Long cityRatio) {
		this.cityRatio = cityRatio;
	}
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	public Long getDateRatio() {
		return dateRatio;
	}
	public void setDateRatio(Long dateRatio) {
		this.dateRatio = dateRatio;
	}
//	public List<ContextContentBean> getPublicContextContent() {
//		return publicContextContent;
//	}
//	public void setPublicContextContent(
//			List<ContextContentBean> publicContextContent) {
//		this.publicContextContent = publicContextContent;
//	}
//	public List<ContextContributedBean> getContributedMementoList() {
//		return contributedMementoList;
//	}
//	public void setContributedMementoList(List<ContextContributedBean> contributedList) {
//		this.contributedMementoList = contributedList;
//	}
//	public List<ContextMediaBean> getMediaList() {
//		return mediaList;
//	}
//	public void setMediaList(List<ContextMediaBean> mediaList) {
//		this.mediaList = mediaList;
//	}
//	public List<ContextEventBean> getEventList() {
//		return eventList;
//	}
//	public void setEventList(List<ContextEventBean> eventList) {
//		this.eventList = eventList;
//	}
//	public List<ContextCreativeWorkBean> getCreativeWorkList() {
//		return creativeWorkList;
//	}
//	public void setCreativeWorkList(List<ContextCreativeWorkBean> creativeWorkList) {
//		this.creativeWorkList = creativeWorkList;
//	}
//	public List<ContextPeopleBean> getFamousPeopleList() {
//		return famousPeopleList;
//	}
//	public void setFamousPeopleList(List<ContextPeopleBean> famousPeopleList) {
//		this.famousPeopleList = famousPeopleList;
//	}
	public List<ContextPublicMementoBean> getPublicMementoList() {
		return publicMementoList;
	}
	public void setPublicMementoList(List<ContextPublicMementoBean> publicMementoList) {
		this.publicMementoList = publicMementoList;
	}
	public String getHashcode() {
		return hashcode;
	}
	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}	
}
