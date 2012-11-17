/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crowdMemories;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author francesco
 */
public class Fuzzy_date implements java.io.Serializable {

    private String fuzzy_date_id;
    private String type_of_date;
    private String decade;
    private String day;
    private String day_name;
    private String day_part;
    private String month;
    private String year;
    private String season;
    private String description_time;
    private String date;
    private String exact_date;
    private String hour;
    private String minute;
    private String second;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = check(hour);
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = check(minute);
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = check(second);
    }

    public Fuzzy_date() {

        type_of_date = "none";
        this.clear_all();
    }

    public String getType_of_date() {
        return type_of_date;
    }

    public void setType_of_date(String type_of_date) {
        this.type_of_date = type_of_date;
        this.clear_all();
    }

    public String getDecade() {
        return decade;
    }

    public void setDecade(String decade) {
        this.decade = check(decade);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = check(day);
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = check(day_name);
    }

    public String getDay_part() {
        return day_part;
    }

    public void setDay_part(String day_part) {
        this.day_part = check(day_part);
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = check(month);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = check(year);
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = check(season);
    }

    public String getDescription_time() {
        return description_time;
    }

    public void setDescription_time(String description_time) {
        this.description_time = check(description_time);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = check(date);
        if (date != null) {
            setExact_date(convert_date(date));
        }
    }

    private void clear_all() {
        decade = null;
        day = null;
        day_name = null;
        day_part = null;
        month = null;
        year = null;
        season = null;
        hour = null;
        minute = null;
        second = null;
        description_time = null;
        date = null;
        exact_date = null;
    }

    public String getExact_date() {
        return exact_date;
    }

    public void setExact_date(String exact_date) {
        this.exact_date = exact_date;
    }

    public String convert_date(String date) {

        String date_convert;
        String[] arr;
        arr = date.split("/");
        date_convert = arr[2] + "-" + arr[0] + "-" + arr[1];

        return date_convert;
    }

    public String check(String s) {
        if (s.equals("")) {
            return null;
        } else {
            return s;
        }
    }
}
