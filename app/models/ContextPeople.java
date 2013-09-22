package models;

import java.util.List;
import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonIgnore;
import play.db.ebean.Model;

@Entity
@Table(name = "Context_Famous")
public class ContextPeople extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000709644175989610L;


	@Id
    @GeneratedValue
    @Column(name="context_famous_id")
    private Long contextItemId;
	@Column
	private String level; // 'WORLD, COUNTRY, REGION'
	@Column
	private Long decade;  
	@Column
	private String type; // 'VIDEO,IMAGE,AUDIO,TEXT'
	@Column
	private String category; // 'PICTURES,SONG,PEOPLE,STORY,FILM,TV,ARTWORK,BOOK,OBJECT' ,
	@Column
	private Long views;
	@Column(name="detail_views")
	private Long detailViews;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "context_id", updatable = true, insertable = true)
	private Context context;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "famous_id", updatable = true, insertable = true)
	private FamousPerson famousPerson;

	public ContextPeople(FamousPerson famousPerson, Context context) {
		super();
		this.setContext(context);
		this.setFamousPerson(famousPerson);
	}
	
	public static Model.Finder<Long, ContextPeople> find = new Finder<Long, ContextPeople>(
			Long.class, ContextPeople.class);

	public static List<ContextPeople> all() {
		return find.all();
	}

	public static void create(ContextPeople contextContributed) {
		contextContributed.save();
	}

	public static ContextPeople createObject(ContextPeople contextContributed) {
		contextContributed.save();
		return contextContributed;
	}

	public static void update(Long contextId, Long famousPersonId) {
		find.where().eq("context.contextId", contextId)
				.eq("famousPerson.famousPersonId", famousPersonId)
				.findUnique()
				.update();
	}

	public static void delete(Long contextId, Long famousPersonId) {
		find.where().eq("context.contextId", contextId)
			.eq("famousPerson.famousPersonId", famousPersonId)
			.findUnique().delete();
	}

	public static ContextPeople read(Long contextId, Long famousPersonId) {
		return find.where().eq("context.contextId", contextId)
				.eq("famousPerson.famousPersonId", famousPersonId)
				.findUnique();
	}

	public static List<ContextPeople> readByContext(Long contextId) {
		List<ContextPeople> contextContributedList = find.where()
				.eq("context.contextId", contextId)
				.findList();
		return contextContributedList;
	}

	public static List<ContextPeople> readByFamousPerson(
			Long famousPersonId) {
		List<ContextPeople> participationList = find.where()
				.eq("famousPerson.famousPersonId", famousPersonId)
				.findList();
		return participationList;
	}

	public static List<ContextPeople> readByPerson(Long personId) {
		Context context = models.Context.findByPerson(personId);
		List<ContextPeople> contexContentList = find.where()
				.eq("context", context)
				.findList();
		return contexContentList;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public FamousPerson getFamousPerson() {
		return famousPerson;
	}

	public void setFamousPerson(FamousPerson famousPerson) {
		this.famousPerson = famousPerson;
	}

	public Long getContextItemId() {
		return contextItemId;
	}

	public void setContextItemId(Long contextItemId) {
		this.contextItemId = contextItemId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Long getDetailViews() {
		return detailViews;
	}

	public void setDetailViews(Long detailViews) {
		this.detailViews = detailViews;
	}

	public Long getDecade() {
		return decade;
	}

	public void setDecade(Long decade) {
		this.decade = decade;
	}
}
