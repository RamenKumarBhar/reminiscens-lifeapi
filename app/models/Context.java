package models;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import play.db.ebean.Model;

@Entity
@Table(name="Context")
public class Context extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2568314759877540078L;

	@Id
    @GeneratedValue
    @Column(name="context_id")
    private Long contextId;

	@Column
	private String title;
	
	@Column
	private String subtitle;
	
	@Column
	private Long personForId;
	
	@ManyToOne
	@MapsId
	@JoinColumn(name="city_for_id")
	private City cityFor;

	@Column(name="city_ratio")
	private Long cityRatio;

	@Temporal(TemporalType.DATE)
	@Column
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime date;

	@Column
	private Long dateRatio;
	
	@Column
	private Boolean enabled;
	
//
//    @ManyToMany
//    @JoinTable(name="Context_Content",
//        joinColumns=
//            @JoinColumn(name="context_id", referencedColumnName="context_id"),
//        inverseJoinColumns=
//            @JoinColumn(name="context_index_id", referencedColumnName="context_index_id")
//        )
//    public List<PublicMemento> publicMementoList;
	
//	@JsonIgnore
//	@OneToMany(mappedBy="context")
//	private List<ContextContent> publicContextContent;
	@JsonIgnore
	@OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
	private List<ContextPublicMemento> publicMementoList;
	
	@JsonIgnore
	@OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
	private List<ContextContributed> contributedMementoList;
	
	@JsonIgnore
	@OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
	private List<ContextMedia> mediaList;

	@JsonIgnore
	@OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
	private List<ContextEvent> eventList;

	@JsonIgnore
	@OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
	private List<ContextCreativeWork> creativeWorkList;

	@JsonIgnore
	@OneToMany(mappedBy = "context", cascade = CascadeType.ALL)
	private List<ContextPeople> famousPeopleList;
	
	public static Model.Finder<Long,Context> find = new Model.Finder<Long, Context>(
            Long.class,Context.class
    );
	
    
    public static List<Context> all(){
        return find.all();
    }
    
    public static void create(Context context){
    	// 1. save contributedMementos
//    	for (ContextContributed contributed : context.getContributedMementos()) {
//			ContextContributed.create(contributed);
//		}
//    	
    	// 2. save contextMedias
    	// TODO implement save one by one if cascade does not work
    	// 3. save contextWorks
    	// TODO implement save one by one if cascade does not work
    	// 3. save contextPeople
    	// TODO implement save one by one if cascade does not work
    	// 4. save contextEvents
    	// TODO implement save one by one if cascade does not work
        context.save();
        context.refresh();
    }
    
    public static Context read(Long contextId) {
    	return find.ref(contextId);
    }
    
    public static Context createObject(Context context){
        context.save();
        return context;
    }
    
    public static void delete(Long id){
        find.ref(id).delete();
    }
    
    public static void update(Long id){
        find.ref(id).update();
    }

    // TODO change model to keep the official context with a "flag"
    public static Context findByPerson(Long personId) {
    	List<Context> contextOfPerson = find.where()
				.eq("personForId", personId)
				.eq("enabled",true)
				.orderBy("publicMementoList.ranking desc")
				.findList();
		return contextOfPerson != null && !contextOfPerson.isEmpty() ? contextOfPerson.get(contextOfPerson.size()-1) : null;
    }
 
    // TODO fix
	public static Context findByPersonAndDecade(Long id, Long decade) {
		List<Context> contextOfPerson = find.where()
				.eq("personForId", id)
				.eq("enabled",true)
				.eq("publicMementoList.decade",decade)
//				.eq("contributedMementoList.decade",decade)
//				.eq("mediaList.decade",decade)
//				.eq("eventList.decade",decade)
//				.eq("creativeWorkList.decade",decade)
//				.eq("famousPeopleList.decade",decade)
				.orderBy("publicMementoList.ranking desc")
				.findList();
		return contextOfPerson != null && !contextOfPerson.isEmpty() ? contextOfPerson.get(contextOfPerson.size()-1) : null;
	}
	// TODO fix
	public static Context findByPersonAndDecadeAndCategory(Long id, Long decade, String category) {
    	List<Context> contextOfPerson = find.where()
				.eq("personForId", id)
				.eq("enabled",true)
				.eq("publicMementoList.decade",decade)
//				.eq("contributedMementoList.decade",decade)
//				.eq("mediaList.decade",decade)
//				.eq("eventList.decade",decade)
//				.eq("creativeWorkList.decade",decade)
//				.eq("famousPeopleList.decade",decade)
				.eq("publicMementoList.category",category)
//				.eq("contributedMementoList.category",category)
//				.eq("mediaList.category",category)
//				.eq("eventList.category",category)
//				.eq("creativeWorkList.category",category)
//				.eq("famousPeopleList.category",category)
				.orderBy("publicMementoList.ranking desc")
				.findList();
		return contextOfPerson != null && !contextOfPerson.isEmpty() ? contextOfPerson.get(contextOfPerson.size()-1) : null;
	}	
	
	// TODO 
	public static Context findByIdAndDecade(Long id, Long decade) {
    	List<Context> contextOfPerson = find.where()
				.eq("contextId", id)
				.eq("enabled",true)
				.eq("publicMementoList.decade",decade)
//				.eq("contributedMementoList.decade",decade)
//				.eq("mediaList.decade",decade)
//				.eq("eventList.decade",decade)
//				.eq("creativeWorkList.decade",decade)
//				.eq("famousPeopleList.decade",decade)
				.orderBy("publicMementoList.ranking desc")
				.findList();
		return contextOfPerson != null && !contextOfPerson.isEmpty() ? contextOfPerson.get(contextOfPerson.size()-1) : null;
	}
	
	// TODO 
	public static Context findByIdAndDecadeAndCategory(Long id, Long decade, String category) {
    	List<Context> contextOfPerson = find.where()
				.eq("contextId", id)
				.eq("enabled",true)
				.eq("publicMementoList.decade",decade)
//				.eq("contributedMementoList.decade",decade)
//				.eq("mediaList.decade",decade)
//				.eq("eventList.decade",decade)
//				.eq("creativeWorkList.decade",decade)
//				.eq("famousPeopleList.decade",decade)
				.eq("publicMementoList.category",category)
//				.eq("contributedMementoList.category",category)
//				.eq("mediaList.category",category)
//				.eq("eventList.category",category)
//				.eq("creativeWorkList.category",category)
//				.eq("famousPeopleList.category",category)
				.orderBy("publicMementoList.ranking desc")
				.findList();
		return contextOfPerson != null && !contextOfPerson.isEmpty() ? contextOfPerson.get(contextOfPerson.size()-1) : null;
	}
	
    public static List<Context> findByCity(Long cityId) {
    	List<Context> contextsForCity = find.where()
				.eq("cityForId", cityId).findList();
		return contextsForCity;
    }
    
    public static void disableContextsForPerson(Long personId) {
    	List<Context> contexts = find.where()
				.eq("personForId", personId)
				.eq("enabled", true)
				.findList();
    	
    	for (Context context : contexts) {
			context.setEnabled(false);
			context.update();
		}
    	
    }

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

	public City getCityFor() {
		return cityFor;
	}

	public void setCityFor(City cityFor) {
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

	public List<ContextContributed> getContributedMementoList() {
		return contributedMementoList;
	}

	public void setContributedMementoList(List<ContextContributed> contributedMementos) {
		this.contributedMementoList = contributedMementos;
	}

	public List<ContextMedia> getMediaList() {
		return mediaList;
	}

	public void setMediaList(List<ContextMedia> mediaList) {
		this.mediaList = mediaList;
	}

	public List<ContextEvent> getEventList() {
		return eventList;
	}

	public void setEventList(List<ContextEvent> eventList) {
		this.eventList = eventList;
	}

	public List<ContextCreativeWork> getCreativeWorkList() {
		return creativeWorkList;
	}

	public void setCreativeWorkList(List<ContextCreativeWork> creativeWorkList) {
		this.creativeWorkList = creativeWorkList;
	}

	public List<ContextPeople> getFamousPeopleList() {
		return famousPeopleList;
	}

	public void setFamousPeopleList(List<ContextPeople> famousPeopleList) {
		this.famousPeopleList = famousPeopleList;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<ContextPublicMemento> getPublicMementoList() {
		return publicMementoList;
	}

	public void setPublicMementoList(List<ContextPublicMemento> publicMementoList) {
		this.publicMementoList = publicMementoList;
	}
}
