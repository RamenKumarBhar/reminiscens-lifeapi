package models;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import play.db.ebean.Model;

@Entity
@Table(name = "Context_Public_Memento")
public class ContextPublicMemento extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000709644175989610L;


	@Id
    @GeneratedValue
    @Column(name="context_public_memento_id")
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
	@Column
	private Long ranking;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "context_id", updatable = true, insertable = true)
	private Context context;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "public_memento_id", updatable = true, insertable = true)
	private PublicMemento publicMemento;

	public ContextPublicMemento(PublicMemento publicMemento, Context context) {
		super();
		this.setContext(context);
		this.setPublicMemento(publicMemento);
	}
	
	public static Model.Finder<Long, ContextPublicMemento> find = new Finder<Long, ContextPublicMemento>(
			Long.class, ContextPublicMemento.class);

	public static List<ContextPublicMemento> all() {
		return find.all();
	}

	public static void create(ContextPublicMemento contextContributed) {
		PublicMemento p = contextContributed.getPublicMemento();
		if (p != null) {
			Long pid = p.getPublicMementoId();
			if (pid==null) {
				PublicMemento.create(p);
			}
		}
		contextContributed.save();
		contextContributed.refresh();
	}

	public static ContextPublicMemento createObject(ContextPublicMemento contextContributed) {
		contextContributed.save();
		return contextContributed;
	}

	public static ContextPublicMemento update(Long contextId, Long publicMementoId, ContextPublicMemento p) {
		ContextPublicMemento cp = find.where().eq("context.contextId", contextId)
				.eq("publicMemento.publicMementoId", publicMementoId)
				.findUnique();
		p.setContextItemId(cp.getContextItemId());
		p.update();
		return p;
	}
	
	public static ContextPublicMemento update(ContextPublicMemento p) {
		Long id = p.getContextItemId();
		p.update(id);
		p.refresh();
		return p;
	}

	public static void delete(Long contextId, Long publicMementoId) {
		ContextPublicMemento pm = find.where().eq("context.contextId", contextId)
			.eq("publicMemento.publicMementoId", publicMementoId)
			.findUnique();
		pm.delete();
	}

	public static ContextPublicMemento read(Long contextId, Long publicMementoId) {
		return find.where().eq("context.contextId", contextId)
				.eq("publicMemento.publicMementoId", publicMementoId)
				.findUnique();
	}

	public static List<ContextPublicMemento> readByContext(Long contextId) {
		List<ContextPublicMemento> contextContributedList = find.where()
				.eq("context.contextId", contextId)
				.findList();
		return contextContributedList;
	}

	public static List<ContextPublicMemento> readByPublicMemento(
			Long publicMementoId) {
		List<ContextPublicMemento> participationList = find.where()
				.eq("publicMemento.publicMementoId", publicMementoId)
				.findList();
		return participationList;
	}

	public static List<ContextPublicMemento> readByPerson(Long personId) {
		Context context = models.Context.findByPerson(personId);
		List<ContextPublicMemento> contexContentList = find.where()
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

	public PublicMemento getPublicMemento() {
		return publicMemento;
	}

	public void setPublicMemento(PublicMemento publicMemento) {
		this.publicMemento = publicMemento;
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

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}

}
