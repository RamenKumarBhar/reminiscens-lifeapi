package models;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import play.db.ebean.Model;

@Entity
@Table(name = "Context_Event")
public class ContextEvent extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000709644175989610L;


	@Id
    @GeneratedValue
    @Column(name="context_event_id")
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
	@JoinColumn(name = "event_id", updatable = true, insertable = true)
	private Event event;

	public ContextEvent(Event event, Context context) {
		super();
		this.setContext(context);
		this.setEvent(event);
	}
	
	public static Model.Finder<Long, ContextEvent> find = new Finder<Long, ContextEvent>(
			Long.class, ContextEvent.class);

	public static List<ContextEvent> all() {
		return find.all();
	}

	public static void create(ContextEvent contextContributed) {
		contextContributed.save();
	}

	public static ContextEvent createObject(ContextEvent contextContributed) {
		contextContributed.save();
		return contextContributed;
	}

	public static void update(Long contextId, Long eventId) {
		find.where().eq("context.contextId", contextId)
				.eq("event.eventId", eventId)
				.findUnique()
				.update();
	}

	public static void delete(Long contextId, Long eventId) {
		find.where().eq("context.contextId", contextId)
			.eq("event.eventId", eventId)
			.findUnique().delete();
	}

	public static ContextEvent read(Long contextId, Long eventId) {
		return find.where().eq("context.contextId", contextId)
				.eq("event.eventId", eventId)
				.findUnique();
	}

	public static List<ContextEvent> readByContext(Long contextId) {
		List<ContextEvent> contextContributedList = find.where()
				.eq("context.contextId", contextId)
				.findList();
		return contextContributedList;
	}

	public static List<ContextEvent> readByEvent(
			Long eventId) {
		List<ContextEvent> participationList = find.where()
				.eq("event.eventId", eventId)
				.findList();
		return participationList;
	}

	public static List<ContextEvent> readByPerson(Long personId) {
		Context context = models.Context.findByPerson(personId);
		List<ContextEvent> contexContentList = find.where()
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

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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
