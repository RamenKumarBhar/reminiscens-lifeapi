package pojos;

import java.io.Serializable;

import org.joda.time.DateTime;

import utils.JodaDateTime;

public class FamousPersonBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9064036172938322280L;

    private Long famousId;

	// Common attributes to other entities of the knowledge base
	private String source;
	private String sourceUrl;
	private String resourceUrl;
	private String locale;
	private String tags;
	private boolean indexed;
    private String resourceType;
    private String category;
	@JodaDateTime(format = "yyyy-MM-dd HH:mm:ss")
	private DateTime lastUpdate;
	
	// Knowledge Base Famous Person Resources specific attributes
	private String fullname;
	private String firstname;
	private String lastname;
	private String famousFor;
	private FuzzyDateBean startDate;
    private FuzzyDateBean endDate;
    private LocationBean startLocation;
    private LocationBean endLocation;
    private CountryBean country;
	private String status;
	private String creatorType;
	@JodaDateTime(format = "yyyy-MM-dd HH:mm:ss")
	private DateTime creationDate;
	public Long getFamousId() {
		return famousId;
	}
	public void setFamousId(Long famousId) {
		this.famousId = famousId;
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
	public DateTime getLastUpdate() {
		return lastUpdate;
	}
	public String getLastUpdateAsString() {
		return lastUpdate != null ? lastUpdate.toString("yyyy-MM-dd HH:mm:ss") : null;
	}
	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFamousFor() {
		return famousFor;
	}
	public void setFamousFor(String famousFor) {
		this.famousFor = famousFor;
	}
	public CountryBean getCountry() {
		return country;
	}
	public void setCountry(CountryBean country) {
		this.country = country;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatorType() {
		return creatorType;
	}
	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}
	public DateTime getCreationDate() {
		return creationDate;
	}
    public String getCreationDateAsString() {
		return creationDate != null ? creationDate.toString("yyyy-MM-dd HH:mm:ss") : null;
	}
	public void setCreationDate(DateTime dateAdded) {
		this.creationDate = dateAdded;
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
	public FuzzyDateBean getStartDate() {
		return startDate;
	}
	public void setStartDate(FuzzyDateBean startDate) {
		this.startDate = startDate;
	}
	public FuzzyDateBean getEndDate() {
		return endDate;
	}
	public void setEndDate(FuzzyDateBean endDate) {
		this.endDate = endDate;
	}
	public LocationBean getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(LocationBean startLocation) {
		this.startLocation = startLocation;
	}
	public LocationBean getEndLocation() {
		return endLocation;
	}
	public void setEndLocation(LocationBean endLocation) {
		this.endLocation = endLocation;
	}
	
	
}
