package models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.Model;

@Entity
@Table(name="Country")
public class Country extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8279916434556075304L;

	@Id
    @GeneratedValue
    @Column(name="country_id")
    private Long countryId;	

	@Column
	private String short_name;
	
	@Column
	private String long_name; 

	@Column
	private String iso2;

	@Column
	private String iso3;
	
	@Column
	private String numcode;

	@Column(name="un_member")
	private String un_member;
	
	@Column
	private String calling_code;
	
	@Column
	private String cctld;
	
	@Column
	private String spanish_name;

	@Column
	private String italian_name;
	
	@Column
	private String french_name;

	@Column
	private String german_name;
	
	@Column
	private String portuguese_name;

	@Column
	private String english_name;
	
	@Column
	private String locale;
	
	public static Model.Finder<Long,Country> find = new Model.Finder<Long, Country>(
            Long.class,Country.class
    );
    
    public static List<Country> all(){
        return find.all();
    }
    
    public static void create(Country country){
        country.save();
    }
    
    public static Country createObject(Country country){
        country.save();
        return country;
    }
    
    public static void delete(Long id){
        find.ref(id).delete();
    }
    
    public static Country read(Long id){
        return find.byId(id);
    }
    
    public static Country readByName(String name){
        List<Country> countries = find.where().eq("short_name", name).findList();
        return countries != null && !countries.isEmpty() ? countries.get(0) : null;
    }
    
    // TODO change the model to store translations in a separated table
    public static Country readByNameAndLocale(String name, String locale){
		String column = locale.equals("it_IT") ? "italian_name" 
				: locale.equals("es_ES") ? "spanish_name"
						: locale.equals("fr_FR") ? "french_name" 
								: locale.equals("en_US") ? "english_name" 
										: locale.equals("pt_PT") ? "portuguese_name" 
												: locale.equals("de_DE") ? "german_name" : null;
        List<Country> countries = column != null ? find.where().eq(column, name).findList() : null;
        return countries != null && !countries.isEmpty() ? countries.get(0) : null;
    }

    // TODO change the model to store translations in a separated table
    public String getNameByLocale(String locale){
		return locale.equals("it_IT") ? this.italian_name
				: locale.equals("es_ES") ? this.spanish_name
						: locale.equals("fr_FR") ? this.french_name 
								: locale.equals("en_US") ? this.english_name
										: locale.equals("pt_PT") ? this.portuguese_name
												: locale.equals("de_DE") ? this.german_name : null;
    }
    
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public String getLong_name() {
		return long_name;
	}

	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}

	public String getIso2() {
		return iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public String getNumcode() {
		return numcode;
	}

	public void setNumcode(String numcode) {
		this.numcode = numcode;
	}

	public String getUn_member() {
		return un_member;
	}

	public void setUn_member(String un_member) {
		this.un_member = un_member;
	}

	public String getCalling_code() {
		return calling_code;
	}

	public void setCalling_code(String calling_code) {
		this.calling_code = calling_code;
	}

	public String getCctld() {
		return cctld;
	}

	public void setCctld(String cctld) {
		this.cctld = cctld;
	}

	public String getSpanish_name() {
		return spanish_name;
	}
	
	public void setSpanish_name(String spanish_name) {
		this.spanish_name = spanish_name;
	}

	public String getItalian_name() {
		return italian_name;
	}

	public void setItalian_name(String italian_name) {
		this.italian_name = italian_name;
	}

	public String getFrench_name() {
		return french_name;
	}

	public void setFrench_name(String french_name) {
		this.french_name = french_name;
	}

	public String getGerman_name() {
		return german_name;
	}

	public void setGerman_name(String german_name) {
		this.german_name = german_name;
	}

	public String getPortuguese_name() {
		return portuguese_name;
	}

	public void setPortuguese_name(String portuguese_name) {
		this.portuguese_name = portuguese_name;
	}

	public String getEnglish_name() {
		return english_name;
	}

	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}

	public static String getFieldForLocale(String locale) {
		return locale.equals("it_IT") ? "italian_name" 
						: locale.equals("es_ES") ? "spanish_name"
								: locale.equals("fr_FR") ? "french_name" 
										: locale.equals("en_US") ? "english_name" 
												: locale.equals("pt_PT") ? "portuguese_name" 
														: locale.equals("de_DE") ? "german_name" : "short_name";
	}
}
