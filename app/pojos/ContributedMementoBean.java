package pojos;

import java.io.Serializable;


public class ContributedMementoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6095216262836069762L;
	
	private Long contributedMementoId;
	private String headline;
    private String text;
    private String type;
    private String category;
    private String source;
    private String sourceUrl;
    private String resourceUrl;
    private String resourceThumbnailUrl;
    private String author;
    private String collection;
    private String nationality;
    private String locale;
    private String haschcode;
    private String fileHashcode;
    private String addedBy;
    
    private FuzzyDateBean startDate;
    
    private LocationBean startLocation;
    
    private FuzzyDateBean endDate;
    
    private LocationBean endLocation;

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

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public FuzzyDateBean getStartDate() {
		return startDate;
	}

	public void setStartDate(FuzzyDateBean startDate) {
		this.startDate = startDate;
	}

	public LocationBean getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(LocationBean startLocation) {
		this.startLocation = startLocation;
	}

	public FuzzyDateBean getEndDate() {
		return endDate;
	}

	public void setEndDate(FuzzyDateBean endDate) {
		this.endDate = endDate;
	}

	public LocationBean getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(LocationBean endLocation) {
		this.endLocation = endLocation;
	}
	
    
    
	
}
