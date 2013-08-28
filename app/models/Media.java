package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.avaje.ebean.ExpressionList;

import enums.MementoCategory;

import play.db.ebean.Model;
import pojos.LocationMinimalBean;

@Entity
@Table(name = "Media")
public class Media extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5265991469293010222L;


	@Id
	@GeneratedValue
	@Column(name = "media_id")
	private Long mediaId;

	@Column(name = "media_url")
	private String resourceUrl;

	@Column(name = "media_type")
	private String type;

	@Column
	private String headline;

	@Column
	private String text;

	@Column
	private String source;

	@Column(name = "source_url")
	private String sourceUrl;

	@Temporal(TemporalType.DATE)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "last_update")
	private DateTime lastUpdate;

	@ManyToOne
	@MapsId
	@JoinColumn(name = "location_id")
	private Location startLocation;

	@ManyToOne
	@MapsId
	@JoinColumn(name = "fuzzy_startdate")
	private FuzzyDate startDate;

	@ManyToOne
	@MapsId
	@JoinColumn(name = "fuzzy_enddate")
	private FuzzyDate endDate;

	@Column
	private String locale;

	@Column
	private String tags;

	@Column
	private boolean indexed;

    @Column (name="resource_type")
    private String resourceType;
    @Column
    private String category;

	public static Model.Finder<Long, Media> find = new Model.Finder<Long, Media>(
			Long.class, Media.class);

	public static List<Media> all() {
		return find.all();
	}

	public static void create(Media media) {
		media.save();
	}

	public static Media createObject(Media media) {
		media.save();
		return media;
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static Media read(Long id) {
		return find.byId(id);
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String mediaUrl) {
		this.resourceUrl = mediaUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String mediaType) {
		this.type = mediaType;
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

	public DateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location location) {
		this.startLocation = location;
	}

	public FuzzyDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FuzzyDate startDate) {
		this.startDate = startDate;
	}

	public FuzzyDate getEndDate() {
		return endDate;
	}

	public void setEndDate(FuzzyDate endDate) {
		this.endDate = endDate;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
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

	public static List<Media> readForContext(String locale,
			MementoCategory category, Long decade,
			List<LocationMinimalBean> locations, String level,
			int itemsPerLevel) {

		List<Media> result = new ArrayList<Media>();
		ExpressionList<Media> el = find.where();
		
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
