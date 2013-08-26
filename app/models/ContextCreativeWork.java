package models;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import play.db.ebean.Model;

@Entity
@Table(name = "Context_Works")
public class ContextCreativeWork extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000709644175989610L;


	@Id
    @GeneratedValue
    @Column(name="context_work_id")
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
	@JoinColumn(name = "work_id", updatable = true, insertable = true)
	private CreativeWork creativeWork;

	public ContextCreativeWork(CreativeWork creativeWork, Context context) {
		super();
		this.setContext(context);
		this.setCreativeWork(creativeWork);
	}
	
	public static Model.Finder<Long, ContextCreativeWork> find = new Finder<Long, ContextCreativeWork>(
			Long.class, ContextCreativeWork.class);

	public static List<ContextCreativeWork> all() {
		return find.all();
	}

	public static void create(ContextCreativeWork contextContributed) {
		contextContributed.save();
	}

	public static ContextCreativeWork createObject(ContextCreativeWork contextContributed) {
		contextContributed.save();
		return contextContributed;
	}

	public static void update(Long contextId, Long creativeWorkId) {
		find.where().eq("context.contextId", contextId)
				.eq("creativeWork.creativeWorkId", creativeWorkId)
				.findUnique()
				.update();
	}

	public static void delete(Long contextId, Long creativeWorkId) {
		find.where().eq("context.contextId", contextId)
			.eq("creativeWork.creativeWorkId", creativeWorkId)
			.findUnique().delete();
	}

	public static ContextCreativeWork read(Long contextId, Long creativeWorkId) {
		return find.where().eq("context.contextId", contextId)
				.eq("creativeWork.creativeWorkId", creativeWorkId)
				.findUnique();
	}

	public static List<ContextCreativeWork> readByContext(Long contextId) {
		List<ContextCreativeWork> contextContributedList = find.where()
				.eq("context.contextId", contextId)
				.findList();
		return contextContributedList;
	}

	public static List<ContextCreativeWork> readByCreativeWork(
			Long creativeWorkId) {
		List<ContextCreativeWork> participationList = find.where()
				.eq("creativeWork.creativeWorkId", creativeWorkId)
				.findList();
		return participationList;
	}

	public static List<ContextCreativeWork> readByPerson(Long personId) {
		Context context = models.Context.findByPerson(personId);
		List<ContextCreativeWork> contexContentList = find.where()
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

	public CreativeWork getCreativeWork() {
		return creativeWork;
	}

	public void setCreativeWork(CreativeWork creativeWork) {
		this.creativeWork = creativeWork;
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
