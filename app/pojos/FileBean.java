package pojos;

import java.io.Serializable;

import org.joda.time.DateTime;

import utils.JodaDateTime;

public class FileBean  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3980159110846013300L;

	private Long fileId;
	private String filename;
	private String URI;
	private String thumbnailURI;
	private String mediumURI;
	private String largeURI;
	private String contentType;
	private String extension; 
	private String hashcode; 
	private Long owner;
	@JodaDateTime(format = "yyyy-MM-dd HH:mm:ss")
	private DateTime creationDate;

	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public Long getOwner() {
		return owner;
	}
	public void setOwner(Long userId) {
		this.owner = userId;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getHashcode() {
		return hashcode;
	}
	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}
	public DateTime getCreationDate() {
		return creationDate;
	}
	public String getCreationDateAsString () {
		return creationDate == null ? null : creationDate.toString("yyyy-MM-dd HH:mm:ss");
	}
	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getThumbnailURI() {
		return thumbnailURI;
	}
	public void setThumbnailURI(String thumbnailURI) {
		this.thumbnailURI = thumbnailURI;
	}
	public String getMediumURI() {
		return mediumURI;
	}
	public void setMediumURI(String mediumURI) {
		this.mediumURI = mediumURI;
	}
	public String getLargeURI() {
		return largeURI;
	}
	public void setLargeURI(String largeURI) {
		this.largeURI = largeURI;
	}
}
