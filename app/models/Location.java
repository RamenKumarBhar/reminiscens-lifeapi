package models;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.db.ebean.Model;

@Entity
@Table(name="Location")
public class Location extends Model {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 775165124786881924L;

	@Id
    @GeneratedValue
    @Column(name="location_id")
    private Long locationId;
	
	@Column
	private String location_textual;
	
	@Column 
	private Long accuracy;
	
	@Column
	private String name;
	
	@Column
	private String description;

	@Column
	private String environment;

	@Column
	private String continent;

	@Column
	private String country;

	@Column
	private String region;

	@Column(name="city")
	private String cityName;

	@Column
	private String neighborhood;

	@Column
	private String street;

	@Column
	private String street_number;

	@Column
	private String map_url;
	
	@Column
	private Integer coordinates_trust;

	@Column
	private Double lat;
	
	@Column
	private Double lon;

	@Column
	private String locale;
	
	@Column
	private Long radius;
	
	@OneToOne
	@MapsId
    @JoinColumn(name="city_id")
	private City city;
	
	// foreign keys
	@JsonIgnore
	@OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
	private List<LifeStory> lifeStoriesLocation;

	@JsonIgnore
	@OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
	private List<Memento> mementosLocation;


	public static Model.Finder<Long,Location> find = new Model.Finder<Long, Location>(
            Long.class,Location.class
    );
    
    public static List<Location> all(){
        return find.all();
    }
    
    public static void create(Location person){
        person.save();
    }
    
    public static Location createOrUpdateIfNotExist(Location location) {
		if (location != null) {
    		Long id = location.getLocationId();
			Location existing = null;
			if (id != null) {
				existing = read(id);
				if (existing.isEqualTo(location)) {
					return existing;
				} else {
					location.copyNotNullsFrom(existing);
				}
			} 
			
			location.setLocationId(null);
			String textual = location.getLocation_textual();
			
			String name = location.getName();
			String country = location.getCountry();
			String region = location.getRegion();
			String cityName = location.getCityName();
			City city = location.getCity();
			Double lat = location.getLat();
			Double lon = location.getLon();

			Long acc = new Long(0); 
//			TODO review the whole accuracy model
//			1, flickr world-level, if we only have textual description (e.g. it was nearby my hometown) or a name of a place
			if ((textual !=null && !textual.isEmpty()) || (name !=null && !name.isEmpty()))
				acc = new Long(1);
			
//			2, if we have only continent or environment (e.g. it was in a beach, it was in africa)
//			3, flickr country-level, if we have country or city or neighbourhood
			if (country !=null && !country.isEmpty())
				acc = new Long(3);
			
//			4, if it is 3 + environment 
//			5, if we have country + city or neighbourhood
//			6, flickr region-level, if we have country + region
			if (region !=null && !region.isEmpty())
				acc = new Long(6);

//			7, if we have 6 + environment
//			11, flickr city-level, if we have country + city 
			if (cityName != null && !cityName.isEmpty()) {
				if (city==null) {
					City c = City.getCityByName(cityName);
					location.setCity(c);
					location.setCountry(c.getCountry().getShort_name());
					location.setRegion(c.getRegion());
					if (lat == null && lon == null) {
						location.setLat(location.getCity().getLat());
						location.setLon(location.getCity().getLon());
						location.setCoordinates_trust(new Integer(0));
						acc = new Long(11);
					}
				}
			}
			
//			12, if we have country + city + neighbourhood
//			16, flickr street-level, if we have 11 or 12 + street
//			17, if we have 7 + environment
//			19,  if we have Lat + Lon but coordinates_trust = false
//			20, the best, if we have both Lat + Longitude and coordinates_trust = true
			if (lat != null && lon!=null) {
				Integer coordTrustInt = location.getCoordinates_trust();
				Boolean coordTrust = coordTrustInt != null && coordTrustInt == 1;
				if (coordTrust) {
					acc = new Long(20);
				} else {
					acc = new Long(19);
				}
			}
			// TODO add geocoding to every new location added			
			
			location.setAccuracy(acc);
			location.save();
			return location;
		} else {
			return null;
		}
	}
    
    private void copyNotNullsFrom(Location existing) {
    	String textual = existing.getLocation_textual();
		String name = existing.getName();
		String country = existing.getCountry();
		String region = existing.getRegion();
		String cityName = existing.getCityName();
		City city = existing.getCity();
		Double lat = existing.getLat();
		Double lon = existing.getLon();
		if (this.location_textual == null) 
			this.location_textual = textual;
		if (this.name == null) 
			this.name = name;
		if (this.country== null) 
			this.country = country;
		if (this.region == null)
			this.region = region;
		if (this.cityName == null)
			this.cityName = cityName;
		if (this.city == null)
			this.city = city; 
		if (this.lat == null)
			this.lat = lat; 
		if (this.lon == null)
			this.lon = lon;		
	}

	private boolean isEqualTo(Location location) {
		String textual = location.getLocation_textual();
		String name = location.getName();
		String country = location.getCountry();
		String region = location.getRegion();
		String cityName = location.getCityName();
		City city = location.getCity();
		Double lat = location.getLat();
		Double lon = location.getLon();

		return this.location_textual == textual 
				&& this.name == name 
				&& this.country == country
				&& this.region == region
				&& this.cityName == cityName
				&& this.city == city
				&& this.lat == lat
				&& this.lon == lon;
	}

	public static Location createObject(Location person){
        person.save();
        return person;
    }
    
    public static void delete(Long id){
        find.ref(id).delete();
    }
    
    public static Location read(Long id){
        return find.byId(id);
    }

	/**
	 * @return the locationId
	 */
	public Long getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the location_textual
	 */
	public String getLocation_textual() {
		return location_textual;
	}

	/**
	 * @param location_textual the location_textual to set
	 */
	public void setLocation_textual(String location_textual) {
		this.location_textual = location_textual;
	}

	/**
	 * @return the accuracy
	 */
	public Long getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(Long  accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * @return the continent
	 */
	public String getContinent() {
		return continent;
	}

	/**
	 * @param continent the continent to set
	 */
	public void setContinent(String continent) {
		this.continent = continent;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		if (getCity() == null) {
			return cityName;
		} else {
			return getCity().getName();
		}
	}

	/**
	 * @param city the city to set
	 */
	public void setCityName(String city) {
		this.cityName = city;
	}

	/**
	 * @return the neighborhood
	 */
	public String getNeighborhood() {
		return neighborhood;
	}

	/**
	 * @param neighborhood the neighborhood to set
	 */
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the street_number
	 */
	public String getStreet_number() {
		return street_number;
	}

	/**
	 * @param street_number the street_number to set
	 */
	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}

	/**
	 * @return the map_url
	 */
	public String getMap_url() {
		return map_url;
	}

	/**
	 * @param map_url the map_url to set
	 */
	public void setMap_url(String map_url) {
		this.map_url = map_url;
	}

	/**
	 * @return the coordinates_trust
	 */
	public Integer getCoordinates_trust() {
		return coordinates_trust;
	}

	/**
	 * @param coordinates_trust the coordinates_trust to set
	 */
	public void setCoordinates_trust(Integer coordinates_trust) {
		this.coordinates_trust = coordinates_trust;
	}

	
	/**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * @return the locate
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locate the locate to set
	 */
	public void setLocale(String locate) {
		this.locale = locate;
	}

	/**
	 * @return the radius
	 */
	public Long getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Long radius) {
		this.radius = radius;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	
}
