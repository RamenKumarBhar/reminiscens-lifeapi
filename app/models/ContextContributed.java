package models;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import play.db.ebean.Model;

@Entity
@Table(name = "Context_Contributed_Memento")
public class ContextContributed extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000709644175989610L;


	@Id
    @GeneratedValue
    @Column(name="context_contributed_id")
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
	@JoinColumn(name = "contributed_memento_id", updatable = true, insertable = true)
	private ContributedMemento contributedMemento;

	public ContextContributed(ContributedMemento contributedMemento, Context context) {
		super();
		this.setContext(context);
		this.setContributedMemento(contributedMemento);
	}
	
	public static Model.Finder<Long, ContextContributed> find = new Finder<Long, ContextContributed>(
			Long.class, ContextContributed.class);

	public static List<ContextContributed> all() {
		return find.all();
	}

	public static void create(ContextContributed contextContributed) {
		contextContributed.save();
		contextContributed.refresh();
	}

	public static ContextContributed createObject(ContextContributed contextContributed) {
		contextContributed.save();
		return contextContributed;
	}

	public static void update(Long contextId, Long contributedMementoId) {
		find.where().eq("context.contextId", contextId)
				.eq("contributedMemento.contributedMementoId", contributedMementoId)
				.findUnique()
				.update();
	}

	public static void delete(Long contextId, Long contributedMementoId) {
		find.where().eq("context.contextId", contextId)
			.eq("contributedMemento.contributedMementoId", contributedMementoId)
			.findUnique().delete();
	}

	public static ContextContributed read(Long contextId, Long contributedMementoId) {
		return find.where().eq("context.contextId", contextId)
				.eq("contributedMemento.contributedMementoId", contributedMementoId)
				.findUnique();
	}

	public static List<ContextContributed> readByContext(Long contextId) {
		List<ContextContributed> contextContributedList = find.where()
				.eq("context.contextId", contextId)
				.findList();
		return contextContributedList;
	}

	public static List<ContextContributed> readByContributedMemento(
			Long contributedMementoId) {
		List<ContextContributed> participationList = find.where()
				.eq("contributedMemento.contributedMementoId", contributedMementoId)
				.findList();
		return participationList;
	}

	public static List<ContextContributed> readByPerson(Long personId) {
		Context context = models.Context.findByPerson(personId);
		List<ContextContributed> contexContentList = find.where()
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

	public ContributedMemento getContributedMemento() {
		return contributedMemento;
	}

	public void setContributedMemento(ContributedMemento contributedMemento) {
		this.contributedMemento = contributedMemento;
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
