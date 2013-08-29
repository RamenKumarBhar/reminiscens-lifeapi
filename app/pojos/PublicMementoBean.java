package pojos;

import java.io.Serializable;

public class PublicMementoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8656034251575759425L;
	private Long publicMementoId;
	private String headline;
	private String text;
	private String type;
	private String resourceType;
	private String category;
	private String source;
	private String sourceUrl;
	private String resourceUrl;
	private String resourceThumbnailUrl;
	private String author;
	private String collection;
	private String collectionType;
	private Boolean isCollection;
	private String nationality;
	private String locale;
	private String haschcode;
	private String fileHashcode;
	private String fileName;
	private String contributorType;
	private Long contributor;
	private FuzzyDateBean startDate;
	private LocationBean startLocation;
	private FuzzyDateBean endDate;
	private LocationBean endLocation;
	private String tags;
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
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
}
