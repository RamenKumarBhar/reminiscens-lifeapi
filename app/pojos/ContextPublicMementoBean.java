package pojos;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ContextPublicMementoBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6003319629318118220L;

	private Long contextItemId;
	private String level; // 'WORLD, COUNTRY, REGION' 
	private Long decade; 
	private String type; // 'VIDEO,IMAGE,AUDIO,TEXT'
	private String category; // 'PICTURES,SONG,PEOPLE,STORY,FILM,TV,ARTWORK,BOOK,OBJECT' ,
	private Long views;
	private Long detailViews;
	private Long stories;
	private Long ranking;
	
	@JsonIgnore
	private ContextBean context;

	private PublicMementoBean publicMemento;

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

	public Long getDecade() {
		return decade;
	}

	public void setDecade(Long decade) {
		this.decade = decade;
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

	public ContextBean getContext() {
		return context;
	}

	public void setContext(ContextBean context) {
		this.context = context;
	}

	public PublicMementoBean getPublicMemento() {
		return publicMemento;
	}

	public void setPublicMemento(PublicMementoBean publicMemento) {
		this.publicMemento = publicMemento;
	}

	public Long getRanking() {
		return ranking;
	}

	public void setRanking(Long ranking) {
		this.ranking = ranking;
	}

	public Long getStories() {
		return stories;
	}

	public void setStories(Long stories) {
		this.stories = stories;
	}
}
