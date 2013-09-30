package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.avaje.ebean.ExpressionList;

import enums.MementoCategory;

import play.db.ebean.Model;
import pojos.LocationMinimalBean;

@Entity
@Table(name = "Public_Memento")
public class PublicMemento extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4527076449581767497L;
	
	@Id
	@GeneratedValue
    @Column(name="public_memento_id")
    private Long publicMementoId;
    @Column
    private String headline;
    @Column
    private String text;
    @Column
    private String type;
    @Column (name="resource_type")
    private String resourceType;
    @Column
    private String category;
    @Column
    private String source;
    @Column(name="source_url")
    private String sourceUrl;
    @Column(name="resource_url")
    private String resourceUrl;
    @Column(name="resource_thumbnail_url")
    private String resourceThumbnailUrl;
    @Column
    private String author;
    @Column
    private String collection;
    @Column(name="collection_type")
    private String collectionType;
    @Column(name="is_collection")
    private Boolean isCollection;
    @Column
    private String nationality;
    @Column
    private String locale;
    @Column
    private String haschcode;
    @Column(name="file_hashcode")
    private String fileHashcode;
    @Column(name="file_name")
    private String fileName;
    @Column(name="contributor_type")
    private String contributorType;
    @Column(name="contributor")
	private Long contributor;
    @Temporal(TemporalType.DATE)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "creation_date")
	private DateTime creationDate;
    @Temporal(TemporalType.DATE)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "last_update")
	private DateTime lastUpdate;
    
    @ManyToOne
	@MapsId
	@JoinColumn(name="fuzzy_startdate", updatable=true, insertable=true)
    private FuzzyDate startDate;
    
    @ManyToOne
	@MapsId
	@JoinColumn(name="location_start_id", updatable=true, insertable=true)
    private Location startLocation;
    
    @ManyToOne
	@MapsId
	@JoinColumn(name="fuzzy_enddate", updatable=true, insertable=true)
    private FuzzyDate endDate;
    
    @ManyToOne
	@MapsId
	@JoinColumn(name="location_end_id", updatable=true, insertable=true)
    private Location endLocation;
    
	@Column
	private boolean indexed;
	
	@Column
	private String tags;

	@Column
	private String migrationId;
    
	@JsonIgnore
	@OneToMany(mappedBy = "publicMemento", cascade = CascadeType.PERSIST)
	private List<ContextPublicMemento> contextPublicMemento;
	
    public static Model.Finder<Long, PublicMemento> find = new Model.Finder<Long, PublicMemento>(
			Long.class, PublicMemento.class);

	public static List<PublicMemento> all() {
		return find.all();
	}
	
	public static PublicMemento read(Long id) {
		return find.ref(id);
	}
	
	public static List<Long> readPublicMementoIds(Long contextId) {
		List<Object> objs = find.where().eq("contextPublicMemento.context.contextId", contextId).findIds();	
		List<Long> result = new ArrayList<Long>();
		for (Object object : objs) {
			result.add((Long)object);
		}
		return result;
	}
	
	public static PublicMemento update(PublicMemento p) {
		Long id = p.getPublicMementoId();
		p.update(id);
		p.refresh();
		return p;
	}


	// TODO Improve efficiency of this process by improving mapping so that 
	// MentionPerson.mentionPersonId generates automatically
	public static void create(PublicMemento publicMemento) {
		// 1. Data to save before creating the new life story
		FuzzyDate start = publicMemento.getStartDate();
		FuzzyDate end = publicMemento.getEndDate();
		Location startPlace = publicMemento.getStartLocation();
		Location endPlace = publicMemento.getEndLocation();
		
		if (start != null)
			publicMemento.setStartDate(FuzzyDate.createOrUpdateIfNotExist(start));
		if (end != null)
			publicMemento.setEndDate(FuzzyDate.createOrUpdateIfNotExist(end));	
		if (startPlace != null)
			publicMemento.setStartLocation(Location.createOrUpdateIfNotExist(startPlace));
		if (endPlace != null)
			publicMemento.setEndLocation(Location.createOrUpdateIfNotExist(endPlace));
		
		publicMemento.setCreationDate(DateTime.now());
		publicMemento.setLastUpdate(DateTime.now());
		publicMemento.save();
		publicMemento.refresh();
	}

	public static PublicMemento createObject(PublicMemento publicMemento) {
		publicMemento.save();
		return publicMemento;
	}
	
	public static List<PublicMemento> readByCountry(String country) {
		

		return null;
	}
	
	

    public static void delete(Long id){
        find.ref(id).delete();
    }
	
	/**
	 * Context creation algorithm
	 * 
	 * @param locale
	 * @param category
	 * @param decade
	 * @param locations
	 * @param level
	 * @param itemsPerLevel
	 * @return
	 */
	public static List<PublicMemento> readForContext(String locale,
			MementoCategory category, Long decade,
			List<LocationMinimalBean> locations, String level,
			int itemsPerLevel, Long contextId) {
		List<Long> exclusion = PublicMemento.readPublicMementoIds(contextId);
		return readForContextWithExclusionList(locale, category, decade, locations, level, itemsPerLevel, exclusion);
	}
	
	
	
	/**
	 * Context creation algorithm
	 * 
	 * @param locale
	 * @param category
	 * @param decade
	 * @param locations
	 * @param level
	 * @param itemsPerLevel
	 * @return
	 */
	public static List<PublicMemento> readForContextWithExclusionList(String locale,
			MementoCategory category, Long decade,
			List<LocationMinimalBean> locations, String level,
			int itemsPerLevel, List<Long> exclusionList) {
		
		List<PublicMemento> result = new ArrayList<PublicMemento>();
		ExpressionList<PublicMemento> el = find.where();
		
		ExpressionList<PublicMemento> el2 = find.where();
		el2.in("publicMementoId", exclusionList);
			
		// prepare query with common parts (locale, category, decade, exclusion lit)
		el.eq("locale", locale)
		.eq("category", category.toString())
		.eq("startDate.decade",decade);
		
		// prepare parts according to the level of accuracy requested
		if (level.equals("WORLD")) {	
			// content from any part of the world excluding those in the location list
			for (LocationMinimalBean loc : locations) {
				String country = loc.getCountry();
				el.ne("startLocation.country", country);
			}
		} else if (level.equals("COUNTRY")) {
			// content related to the countries in the location list
			List<String> countries = new ArrayList<String>();
			for (LocationMinimalBean loc : locations) {
				String country = loc.getCountry();
				countries.add(country);
			}
			el.in("startLocation.country", countries);
		} else if (level.equals("REGION")) {
			// content related to the regions in the location list
			for (LocationMinimalBean loc : locations) {				
				String country = loc.getCountry();
				if (country != null && !country.isEmpty()) {
					el.eq("startLocation.country", country);
					String region = loc.getRegion();
					if (region != null && !region.isEmpty()) {
						el.eq("startLocation.region", region);
					}
				}
			}
		}
		// exclude items in the exclusion list
		for (Long publicMementoId : exclusionList) {
			el.ne("publicMementoId", publicMementoId);
		}
		
		// execute query with random ordering and limiting results according to the number of items requested for this level
		el.orderBy("rand()")
		.setMaxRows(itemsPerLevel);
		result.addAll(el.findList());
		
		return result;
	}

	
	/**
	 * TODO
	 * Context creation algorithm (using criteria builder and exclusion list)
	 * 
	 * @param locale
	 * @param category
	 * @param decade
	 * @param locations
	 * @param level
	 * @param itemsPerLevel
	 * @return
	 */
	public static List<PublicMemento> readForContextWithExclusionListOptimized(String locale,
			MementoCategory category, Long decade,
			List<LocationMinimalBean> locations, String level,
			int itemsPerLevel, List<Long> exclusionList) {
		
		List<PublicMemento> result = new ArrayList<PublicMemento>();
		ExpressionList<PublicMemento> el = find.where();
		
		ExpressionList<PublicMemento> el2 = find.where();
		el2.in("publicMementoId", exclusionList);
		
	//	EntityManager em = JPA.em();
		
//		CriteriaBuilder criteria = em.getCriteriaBuilder();

		
		// prepare query with commong parts (locale, category, decade, exclusion lit)
		el.eq("locale", locale)
		.eq("category", category.toString())
		.eq("startDate.decade",decade);
		
		if (level.equals("WORLD")) {
				el.orderBy("rand()")
				.setMaxRows(itemsPerLevel);
				result.addAll(el.findList());
		} else if (level.equals("COUNTRY")) {
			List<String> countries = new ArrayList<String>();
			for (LocationMinimalBean loc : locations) {
				String country = loc.getCountry();
				countries.add(country);
			}
			el.eq("locale", locale)
			.eq("category", category.toString())
			.eq("startDate.decade",decade)
			.in("startLocation.country", countries)
			.orderBy("rand()")
			.setMaxRows(itemsPerLevel);	
			result.addAll(el.findList());
		} else if (level.equals("REGION")) {
			for (LocationMinimalBean loc : locations) {
				el.eq("locale", locale)
				.eq("category", category.toString())
				.eq("startDate.decade",decade);
				
				String country = loc.getCountry();
				if (country != null && !country.isEmpty()) {
					el.eq("startLocation.country", country);
					String region = loc.getRegion();
					if (region != null && !region.isEmpty()) {
						el.eq("startLocation.region", region);
						el.orderBy("rand()")
						.setMaxRows(itemsPerLevel);		
						result.addAll(el.findList());
					}
				}
			}
		}
		
		
		
		return result;
		
		
	}
	
	
	public Long getPublicMementoId() {
		return publicMementoId;
	}

	public void setPublicMementoId(Long publicMementoId) {
		this.publicMementoId = publicMementoId;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getResourceThumbnailUrl() {
		return resourceThumbnailUrl;
	}

	public void setResourceThumbnailUrl(String resourceThumbnailUrl) {
		this.resourceThumbnailUrl = resourceThumbnailUrl;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public Boolean getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getHaschcode() {
		return haschcode;
	}

	public void setHaschcode(String haschcode) {
		this.haschcode = haschcode;
	}

	public String getFileHashcode() {
		return fileHashcode;
	}

	public void setFileHashcode(String fileHashcode) {
		this.fileHashcode = fileHashcode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContributorType() {
		return contributorType;
	}

	public void setContributorType(String contributorType) {
		this.contributorType = contributorType;
	}

	public Long getContributor() {
		return contributor;
	}

	public void setContributor(Long contributor) {
		this.contributor = contributor;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public DateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public FuzzyDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FuzzyDate startDate) {
		this.startDate = startDate;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public FuzzyDate getEndDate() {
		return endDate;
	}

	public void setEndDate(FuzzyDate endDate) {
		this.endDate = endDate;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getMigrationId() {
		return migrationId;
	}

	public void setMigrationId(String migrationId) {
		this.migrationId = migrationId;
	}


	public List<ContextPublicMemento> getContextPublicMemento() {
		return contextPublicMemento;
	}


	public void setContextPublicMemento(
			List<ContextPublicMemento> contextPublicMemento) {
		this.contextPublicMemento = contextPublicMemento;
	}
}
