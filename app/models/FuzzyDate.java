package models;

import java.text.ParseException;
import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import play.db.ebean.Model;

@Entity
@Table(name = "Fuzzy_Date")
public class FuzzyDate extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5986577686511527227L;

	@Id
	@GeneratedValue
	@Column(name = "fuzzy_date_id")
	private Long fuzzyDateId;

	@Column
	private String textual_date;

	@Temporal(TemporalType.DATE)
	@Column
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime exactDate;

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

	// Foreign Keys
	@JsonIgnore
	@OneToMany(mappedBy = "startDate", cascade = CascadeType.ALL)
	private List<LifeStory> lifeStoriesStart;

	@JsonIgnore
	@OneToMany(mappedBy = "endDate", cascade = CascadeType.ALL)
	private List<LifeStory> lifeStoriesEnd;

	@JsonIgnore
	@OneToMany(mappedBy = "startDate", cascade = CascadeType.ALL)
	private List<Memento> mementosStart;

	@JsonIgnore
	@OneToMany(mappedBy = "endDate", cascade = CascadeType.ALL)
	private List<Memento> mementosEnd;
	
	
	// Foreign Keys
	@JsonIgnore
	@OneToMany(mappedBy = "startDate", cascade = CascadeType.ALL)
	private List<PublicMemento> publicMementoStart;

	@JsonIgnore
	@OneToMany(mappedBy = "endDate", cascade = CascadeType.ALL)
	private List<PublicMemento> publicMementoEnd;


	public static Model.Finder<Long, FuzzyDate> find = new Model.Finder<Long, FuzzyDate>(
			Long.class, FuzzyDate.class);

	public static List<FuzzyDate> all() {
		return find.all();
	}

	public static void create(FuzzyDate fuzzyDate) {
		fuzzyDate.save();
	}

	public static FuzzyDate createOrUpdateIfNotExist(FuzzyDate fuzzyDate) {
		if (fuzzyDate != null) {
			Long id = fuzzyDate.getFuzzyDateId();
			DateTime exactDate = fuzzyDate.getExactDate();

			// 1. if a record for this date exists, reuse
			FuzzyDate existing = null;
			if (id != null) {
				existing = read(id);
				// if the existing date with the provided id is equal, there is
				// nothing to create or update
				if (existing != null && existing.isEqualTo(fuzzyDate)) {
					return existing;
				}
			} else if (exactDate != null) {
				existing = readByExactDate(exactDate);
				// if the existing date with the provided id is equal, there is
				// nothing to create or update
				if (existing != null && existing.isEqualTo(fuzzyDate)) {
					return existing;
				}
			}
			// to ensure we create a new one, we don't update existing fuzzy
			// dates
			fuzzyDate.setFuzzyDateId(null);

			// 2. make sure all important fields are compiled
			// fuzzy date parts
			Long decade = fuzzyDate.getDecade();
			Long year = fuzzyDate.getYear();
			String month = fuzzyDate.getMonth();
			String day = fuzzyDate.getDay();
			String hour = fuzzyDate.getHour();
			String minute = fuzzyDate.getMinute();
			String second = fuzzyDate.getSecond();
			String textual = fuzzyDate.getTextual_date();

			// 0, if we only have a textual description (e.g.it was around ten
			// or 15 years ago,when i was on summer vacation)
			// 1-7, number of date parts we have (decade, year, month, day,
			// hour, minute, second)
			// 8, we have additional info
			// 9, only textual
			Long acc = new Long(0);

			// if the exact date is present, fill all the blanks
			if (exactDate != null) {
				year = new Long(exactDate.getYear());
				month = exactDate.getMonthOfYear() + "";
				day = exactDate.getDayOfMonth() + "";
				hour = exactDate.getHourOfDay() + "";
				minute = exactDate.getMinuteOfHour() + "";
				second = exactDate.getSecondOfMinute() + "";
				decade = year - year % 10;

				fuzzyDate.setDecade(decade);
				fuzzyDate.setYear(year);
				fuzzyDate.setMonth(month);
				fuzzyDate.setDay(day);
				fuzzyDate.setHour(hour);
				fuzzyDate.setMinute(minute);
				fuzzyDate.setSecond(second);
				fuzzyDate.setAccuracy(new Long(7));
			} else {
				if (year != null && year > 0) {
					decade = year - year % 10; // make sure decade correspond to
												// year
					acc++;
				}
				// sync year and decade
				if (decade != null && decade > 0)
					acc++;

				if (month != null && !month.isEmpty())
					acc++;

				if (day != null && !day.isEmpty())
					acc++;

				if (hour != null && !hour.isEmpty())
					acc++;

				if (minute != null && !minute.isEmpty())
					acc++;

				if (second != null && !second.isEmpty())
					acc++;
			}

			if (textual != null && textual.isEmpty()) {
				if (acc > 0) {
					acc++;
				} else {
					acc = new Long(9);
				}
			}

			fuzzyDate.save();
			return fuzzyDate;
		} else {
			return null;
		}
	}

	private boolean isEqualTo(FuzzyDate fd) {
		DateTime exact = fd.getExactDate();
		Long decade = fd.getDecade();
		Long year = fd.getYear();
		String month = fd.getMonth();
		String day = fd.getDay();
		String hour = fd.getHour();
		String minute = fd.getMinute();
		String second = fd.getSecond();
		String textual = fd.getTextual_date();

		boolean result = (this.exactDate == exact);
		if (!result) {
			result = (this.decade == decade) && (this.year == year)
					&& (this.month == month) && (this.day == day)
					&& (this.hour == hour) && (this.minute == minute)
					&& (this.second == second)
					&& (this.textual_date == textual);
		}
		return result;
	}

	public static FuzzyDate createObject(FuzzyDate fuzzyDate) {
		fuzzyDate.save();
		return fuzzyDate;
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}

	public static FuzzyDate read(Long id) {
		return find.byId(id);
	}

	public static FuzzyDate readByExactDate(DateTime exactDate) {
		List<FuzzyDate> fList = find.where().eq("exactDate", exactDate)
				.findList();

		return fList != null && !fList.isEmpty() ? fList.get(0) : null;
	}

	/**
	 * @return the fuzzyDateId
	 */
	public Long getFuzzyDateId() {
		return fuzzyDateId;
	}

	/**
	 * @param fuzzyDateId
	 *            the fuzzyDateId to set
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
	 * @param textual_date
	 *            the textual_date to set
	 */
	public void setTextual_date(String textual_date) {
		this.textual_date = textual_date;
	}

	/**
	 * @return the exact_date
	 */
	public DateTime getExactDate() {
		// String pattern = "yyyy-MM-dd HH:mm:ss";
		// SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return exactDate;
	}

	/**
	 * @param exact_date
	 *            the exact_date to set
	 * @throws ParseException
	 */
	public void setExactDate(DateTime exact_date) throws ParseException {
		// String pattern = "yyyy-MM-dd HH:mm:ss";
		// SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		this.exactDate = exact_date;
	}

	/**
	 * @return the decade
	 */
	public Long getDecade() {
		return decade;
	}

	/**
	 * @param decade
	 *            the decade to set
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
	 * @param year
	 *            the year to set
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
	 * @param season
	 *            the season to set
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
	 * @param month
	 *            the month to set
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
	 * @param day
	 *            the day to set
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
	 * @param day_name
	 *            the day_name to set
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
	 * @param day_part
	 *            the day_part to set
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
	 * @param hour
	 *            the hour to set
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
	 * @param minute
	 *            the minute to set
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
	 * @param second
	 *            the second to set
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
	 * @param accuracy
	 *            the accuracy to set
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
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	public List<LifeStory> getLifeStoriesStart() {
		return lifeStoriesStart;
	}

	public void setLifeStoriesStart(List<LifeStory> lifeStoriesStart) {
		this.lifeStoriesStart = lifeStoriesStart;
	}

	public List<LifeStory> getLifeStoriesEnd() {
		return lifeStoriesEnd;
	}

	public void setLifeStoriesEnd(List<LifeStory> lifeStoriesEnd) {
		this.lifeStoriesEnd = lifeStoriesEnd;
	}

	public List<Memento> getMementosStart() {
		return mementosStart;
	}

	public void setMementosStart(List<Memento> mementosStart) {
		this.mementosStart = mementosStart;
	}

	public List<Memento> getMementosEnd() {
		return mementosEnd;
	}

	public void setMementosEnd(List<Memento> mementosEnd) {
		this.mementosEnd = mementosEnd;
	}

	public List<PublicMemento> getPublicMementoStart() {
		return publicMementoStart;
	}

	public void setPublicMementoStart(List<PublicMemento> publicMementoStart) {
		this.publicMementoStart = publicMementoStart;
	}

	public List<PublicMemento> getPublicMementoEnd() {
		return publicMementoEnd;
	}

	public void setPublicMementoEnd(List<PublicMemento> publicMementoEnd) {
		this.publicMementoEnd = publicMementoEnd;
	}

}
