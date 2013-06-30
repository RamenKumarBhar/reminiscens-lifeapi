package models;

import java.util.List;

import javax.persistence.*;

import org.joda.time.DateTime;

import play.db.ebean.Model;

@Entity
@Table(name="Fuzzy_Date")
public class FuzzyDate extends Model {

	
	@Id
    @GeneratedValue
    @Column(name="fuzzy_date_id")
    private Long fuzzyDateId;
	
	@Column
	private String textual_date;

	@Column
	@DateTime(format="yyyy-dd-mm hh:mm:ss");
	private DateTime exact_date;

	@Column
	private Long decade;

	@Column
	private Long year;

	@Column
	private String season;

	@Column
	private String month;

	@Column
	private String day;

	@Column
	private String day_name;

	@Column
	private String day_part;

	@Column
	private String hour;

	@Column
	private String minute;
	
	@Column
	private String second;
	
	@Column
	private Long accuracy;
	
	@Column
	private String locale;
	

	public static Model.Finder<Long,FuzzyDate> find = new Model.Finder(
            Long.class,FuzzyDate.class
    );
    
    public static List<FuzzyDate> all(){
        return find.all();
    }
    
    public static void create(FuzzyDate person){
        person.save();
    }
    
    public static FuzzyDate createObject(FuzzyDate person){
        person.save();
        return person;
    }
    
    public static void delete(Long id){
        find.ref(id).delete();
    }
    
    public static FuzzyDate read(Long id){
        return find.byId(id);
    }

	/**
	 * @return the fuzzyDateId
	 */
	public Long getFuzzyDateId() {
		return fuzzyDateId;
	}

	/**
	 * @param fuzzyDateId the fuzzyDateId to set
	 */
	public void setFuzzyDateId(Long fuzzyDateId) {
		this.fuzzyDateId = fuzzyDateId;
	}

	/**
	 * @return the textual_date
	 */
	public String getTextual_date() {
		return textual_date;
	}

	/**
	 * @param textual_date the textual_date to set
	 */
	public void setTextual_date(String textual_date) {
		this.textual_date = textual_date;
	}

	/**
	 * @return the exact_date
	 */
	public DateTime getExact_date() {
		return exact_date;
	}

	/**
	 * @param exact_date the exact_date to set
	 */
	public void setExact_date(DateTime exact_date) {
		this.exact_date = exact_date;
	}

	/**
	 * @return the decade
	 */
	public Long getDecade() {
		return decade;
	}

	/**
	 * @param decade the decade to set
	 */
	public void setDecade(Long decade) {
		this.decade = decade;
	}

	/**
	 * @return the year
	 */
	public Long getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Long year) {
		this.year = year;
	}

	/**
	 * @return the season
	 */
	public String getSeason() {
		return season;
	}

	/**
	 * @param season the season to set
	 */
	public void setSeason(String season) {
		this.season = season;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * @return the day_name
	 */
	public String getDay_name() {
		return day_name;
	}

	/**
	 * @param day_name the day_name to set
	 */
	public void setDay_name(String day_name) {
		this.day_name = day_name;
	}

	/**
	 * @return the day_part
	 */
	public String getDay_part() {
		return day_part;
	}

	/**
	 * @param day_part the day_part to set
	 */
	public void setDay_part(String day_part) {
		this.day_part = day_part;
	}

	/**
	 * @return the hour
	 */
	public String getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(String hour) {
		this.hour = hour;
	}

	/**
	 * @return the minute
	 */
	public String getMinute() {
		return minute;
	}

	/**
	 * @param minute the minute to set
	 */
	public void setMinute(String minute) {
		this.minute = minute;
	}

	/**
	 * @return the second
	 */
	public String getSecond() {
		return second;
	}

	/**
	 * @param second the second to set
	 */
	public void setSecond(String second) {
		this.second = second;
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
	public void setAccuracy(Long accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}


}