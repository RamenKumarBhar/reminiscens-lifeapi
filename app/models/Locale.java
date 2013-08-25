package models;


import java.util.List;

import javax.persistence.*;

import play.db.ebean.Model;

@Entity
@Table(name="Locale")
public class Locale extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5305166008974965328L;
	@Id
    @GeneratedValue
    @Column(name="locale_id")
	private Long localeId;
	@Column
	private String code;
	@Column
	private String description;
	@Column
	private String language;
	@Column
	private String code2;
	
	public static Model.Finder<Long,Locale> find = new Model.Finder<Long, Locale>(
            Long.class,Locale.class
    );
    
    public static List<Locale> all(){
        return find.all();
    }
    
    public static void create(Locale locale){
        locale.save();
    }
    
    public static Locale createObject(Locale locale){
        locale.save();
        return locale;
    }
    
    public static void delete(Long id){
        find.ref(id).delete();
    }
    
    public static Locale read(Long id){
        return find.byId(id);
    }
    
    public static Locale readByCode(String code){
        List<Locale> locales = find.where().eq("code", code).findList();
        return locales != null && !locales.isEmpty() ? locales.get(0) : null;
    }

    public static Locale readByCode2(String code2){
        List<Locale> locales = find.where().eq("code2", code2).findList();
        return locales != null && !locales.isEmpty() ? locales.get(0) : null;
    }

    public static List<Locale> readByLanguageCode(String lang){
        return find.where().eq("language", lang).findList();
    }
    
    public static Locale readByLanguageAndCountryCode(String lang, String countryCode){
    	String langCode = lang+"_"+countryCode;
        List<Locale> locales = find.where().eq("code", langCode).findList();
        return locales != null && !locales.isEmpty() ? locales.get(0) : null;
    }
	
	public Long getLocaleId() {
		return localeId;
	}
	public void setLocaleId(Long localeId) {
		this.localeId = localeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}


	
}
