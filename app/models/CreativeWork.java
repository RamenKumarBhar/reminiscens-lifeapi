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
@Table(name = "Works")
public class CreativeWork extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5888693895616346277L;

	@Id
	@GeneratedValue
	@Column(name = "work_id")
	private Long workId;

	@Column(name = "title")
	private String headline;

	@Column(name = "description")
	private String text;

	@Column
	private String author;

	@OneToOne
	@MapsId
	@JoinColumn(name = "author_country_id")
	private Country authorCountry;

	@Column
	private String collection;

	@Column
	private String type;

	@Column
	private String source;

	@Column
	private String locale;

	@Column(name = "source_url")
	private String sourceUrl;

	@Column(name = "resource_url")
	private String resourceUrl;

	@ManyToOne
	@MapsId
	@JoinColumn(name = "fuzzy_releasedate")
	private FuzzyDate startDate;

	@Column
	private String tags;

	@Column
	private boolean indexed;
    @Column (name="resource_type")
    private String resourceType;
    @Column
    private String category;

	@Temporal(TemporalType.DATE)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "last_update")
	private DateTime lastUpdate;

	public static Model.Finder<Long, CreativeWork> find = new Model.Finder<Long, CreativeWork>(
			Long.class, CreativeWork.class);

	public static List<CreativeWork> all() {
		return find.all();
	}

	public static void create(CreativeWork creativeWork) {
		creativeWork.save();
	}

	public static CreativeWork createObject(CreativeWork creativeWork) {
		creativeWork.save();
		return creativeWork;
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static CreativeWork read(Long id) {
		return find.byId(id);
	}

	public Long getWorkId() {
		return workId;
	}

	public void setWorkId(Long workId) {
		this.workId = workId;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Country getAuthorCountry() {
		return authorCountry;
	}

	public void setAuthorCountry(Country authorCountry) {
		this.authorCountry = authorCountry;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
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

	public FuzzyDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FuzzyDate releaseDate) {
		this.startDate = releaseDate;
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

	public DateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	public static List<CreativeWork> readForContext(String locale,
			MementoCategory category, Long decade,
			List<LocationMinimalBean> locations, String level,
			int itemsPerLevel) {

		List<CreativeWork> result = new ArrayList<CreativeWork>();
		ExpressionList<CreativeWork> el = find.where();
		
		if (level.equals("WORLD")) {
			el.eq("locale", locale)
				.eq("category", category.toString())
				.eq("startDate.decade",decade)
				.orderBy("rand()")
				.setMaxRows(itemsPerLevel);
				result.addAll(el.findList());
		} else if (level.equals("COUNTRY")) {
			List<String> countries = new ArrayList<String>();
			String countryField = Country.getFieldForLocale(locale);
			for (LocationMinimalBean loc : locations) {
				String country = loc.getCountry();
				countries.add(country);
			}
			el.eq("locale", locale)
			.eq("category", category.toString())
			.eq("startDate.decade",decade)
			.in("authorCountry."+countryField, countries)
			.orderBy("rand()")
			.setMaxRows(itemsPerLevel);	
			result.addAll(el.findList());
		} else if (level.equals("REGION")) {
			// TODO
		}
		return result;
	}
}
