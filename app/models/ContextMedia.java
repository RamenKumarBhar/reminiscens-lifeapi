package models;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import play.db.ebean.Model;

@Entity
@Table(name = "Context_Media")
public class ContextMedia extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000709644175989610L;


	@Id
    @GeneratedValue
    @Column(name="context_media_id")
    private Long contextItemId;
	@Column
	private String level; // 'WORLD, COUNTRY, REGION' 
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
	@JoinColumn(name = "media_id", updatable = false, insertable = false)
	private Media media;

	public ContextMedia(Media media, Context context) {
		super();
		this.setContext(context);
		this.setMedia(media);
	}
	
	public static Model.Finder<Long, ContextMedia> find = new Finder<Long, ContextMedia>(
			Long.class, ContextMedia.class);

	public static List<ContextMedia> all() {
		return find.all();
	}

	public static void create(ContextMedia contextContributed) {
		contextContributed.save();
	}

	public static ContextMedia createObject(ContextMedia contextContributed) {
		contextContributed.save();
		return contextContributed;
	}

	public static void update(Long contextId, Long mediaId) {
		find.where().eq("context.contextId", contextId)
				.eq("media.mediaId", mediaId)
				.findUnique()
				.update();
	}

	public static void delete(Long contextId, Long mediaId) {
		find.where().eq("context.contextId", contextId)
			.eq("media.mediaId", mediaId)
			.findUnique().delete();
	}

	public static ContextMedia read(Long contextId, Long mediaId) {
		return find.where().eq("context.contextId", contextId)
				.eq("media.mediaId", mediaId)
				.findUnique();
	}

	public static List<ContextMedia> readByContext(Long contextId) {
		List<ContextMedia> contextContributedList = find.where()
				.eq("context.contextId", contextId)
				.findList();
		return contextContributedList;
	}

	public static List<ContextMedia> readByMedia(
			Long mediaId) {
		List<ContextMedia> participationList = find.where()
				.eq("media.mediaId", mediaId)
				.findList();
		return participationList;
	}

	public static List<ContextMedia> readByPerson(Long personId) {
		Context context = models.Context.findByPerson(personId);
		List<ContextMedia> contexContentList = find.where()
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

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
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

}
