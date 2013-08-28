package pojos;

import java.io.Serializable;

public class LocationBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 134847973176532044L;
	
	private Long locationId;
	private String textual; 
	
	private String placeName;
	private String country;
	private String region;
	private String city;
	
	private Integer coordinates_trust;
	private Double lat;
	private Double lon;
	
	private String locale;

	private CityBean cityBean;
	
	private Long accuracy;

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getTextual() {
		return textual;
	}

	public void setTextual(String textual) {
		this.textual = textual;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCoordinates_trust() {
		return coordinates_trust;
	}

	public void setCoordinates_trust(Integer coordinates_trust) {
		this.coordinates_trust = coordinates_trust;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public CityBean getCityBean() {
		return cityBean;
	}

	public void setCityBean(CityBean cityBean) {
		this.cityBean = cityBean;
	}

	public Long getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Long accuracy) {
		this.accuracy = accuracy;
	}
	

}
