package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import com.avaje.ebean.ExpressionList;

import enums.MementoCategory;

import play.db.ebean.Model;
import pojos.LocationMinimalBean;

@Entity
@Table(name = "Contributed_Memento")
public class ContributedMemento extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4527076449581767497L;
	
	@Id
	@GeneratedValue
    @Column(name="contributed_memento_id")
    private Long contributedMementoId;
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
    @Column
    private String nationality;
    @Column
    private String locale;
    @Column
    private String haschcode;
    @Column(name="file_hashcode")
    private String fileHashcode;
    @Column(name="added_by")
    private String addedBy;
    
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
    
    public static Model.Finder<Long, ContributedMemento> find = new Model.Finder<Long, ContributedMemento>(
			Long.class, ContributedMemento.class);

	public static List<ContributedMemento> all() {
		return find.all();
	}

	// TODO Improve efficiency of this process by improving mapping so that 
	// MentionPerson.mentionPersonId generates automatically
	public static void create(ContributedMemento contributedMemento) {
		// 1. Data to save before creating the new life story
		FuzzyDate start = contributedMemento.getStartDate();
		FuzzyDate end = contributedMemento.getEndDate();
		Location startPlace = contributedMemento.getStartLocation();
		Location endPlace = contributedMemento.getEndLocation();
		
		if (start != null)
			contributedMemento.setStartDate(FuzzyDate.createOrUpdateIfNotExist(start));
		if (end != null)
			contributedMemento.setEndDate(FuzzyDate.createOrUpdateIfNotExist(end));	
		if (startPlace != null)
			contributedMemento.setStartLocation(Location.createOrUpdateIfNotExist(startPlace));
		if (endPlace != null)
			contributedMemento.setEndLocation(Location.createOrUpdateIfNotExist(endPlace));
		
		contributedMemento.save();
	}

	public static ContributedMemento createObject(ContributedMemento contributedMemento) {
		contributedMemento.save();
		return contributedMemento;
	}
	
	public static List<ContributedMemento> readByCountry(String country) {
		

		return null;
	}
	

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static ContributedMemento read(Long id) {
		return find.byId(id);
	}

	public Long getContributedMementoId() {
		return contributedMementoId;
	}

	public void setContributedMementoId(Long contributedMementoId) {
		this.contributedMementoId = contributedMementoId;
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

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public static List<ContributedMemento> readForContext(String locale,
			MementoCategory category, Long decade,
			List<LocationMinimalBean> locations, String level,
			int itemsPerLevel) {
		
		List<ContributedMemento> result = new ArrayList<ContributedMemento>();
		ExpressionList<ContributedMemento> el = find.where();
		
		if (level.equals("WORLD")) {
			el.eq("locale", locale)
				.eq("category", category.toString())
				.eq("startDate.decade",decade)
				.orderBy("rand()")
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
					}
				}
				el.orderBy("rand()")
				.setMaxRows(itemsPerLevel);		
				result.addAll(el.findList());
			}
		}
		return result;
	}
}
