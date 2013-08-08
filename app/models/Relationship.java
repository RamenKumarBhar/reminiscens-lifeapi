package models;

import java.util.List;

import javax.persistence.*;
import play.db.ebean.*;
import org.codehaus.jackson.annotate.*;

@Entity
@Table(name="Relationship")
public class Relationship extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7045811975198960968L;

	@Id
    @GeneratedValue
    @Column(name="relationship_id")
    private Long relationshipId;	
	
	@Column(name="person_id_to")
    private Long personToId;	
	
	@Column(name="person_id_from")
    private Long personFromId;	
	
	@Column(name="directed")
    private Boolean directed = Boolean.FALSE;
    
	@Column(name="to_is_curator")
    private Boolean toIsCurator = Boolean.FALSE;
    
	@Column(name="from_is_curator")
    private Boolean fromIsCurator = Boolean.FALSE;
    
	@Column(name="role_person_to")
	private String roleTo;
    
	@Column(name="role_person_from")
	private String roleFrom;
    
	@Column
    private String network = "FRIENDS";
    
	
	@JsonIgnore	
	@ManyToOne
	@JoinColumn(name="person_id_to", updatable = false, insertable = false)
    private Person personTo;	
	
	@JsonIgnore	
	@ManyToOne
	@JoinColumn(name="person_id_from", updatable = false, insertable = false)
    private Person personFrom;	
    
	
	public static Model.Finder<Long,Relationship> find = new Model.Finder<Long, Relationship>(
            Long.class,Relationship.class
    );
    
    public static List<Relationship> all(){
        return find.all();
    }
    
    public static void create(Relationship personRel){
    	personRel.save();
    }
    
    public static Relationship createObject(Relationship personRel){
    	personRel.save();
        return personRel;
    }
    
    public static void delete(Long id){
        find.ref(id).delete();
    }
    
    public static Relationship read(Long id){
        return find.byId(id);
    }

    public static List<Relationship> findRelationshipsByPerson(Long id) {
    	List<Relationship> participationList = find.where()
				.eq("personFromId", id).findList();
		return participationList;
    }
    
    public static List<Relationship> findCuratorsOfPerson(Long id) {
		List<Relationship> participationList = find.where()
				.eq("personFromId", id)
				.eq("toIsCurator",Boolean.TRUE)
				.findList();
		return participationList;
    }
    
    public static List<Relationship> findRelationships(Long person1, Long person2) {
    	List<Relationship> participationList = find.where()
				.eq("personFromId", person1)
				.eq("personToId",person2)
				.findList();
    	
    	List<Relationship> participationList2 = find.where()
				.eq("personFromId", person1)
				.eq("personToId",person2)
				.findList();
    	
    	participationList.addAll(participationList2);
		return participationList;
    }
    
	public static boolean isCuratorOf(Long user, Long person) {
		List<Relationship> list = find.where()
			.eq("personFromId",user)
			.eq("personToId", person)
			.eq("toIsCurator",Boolean.TRUE)
			.findList();
		return list != null && list.size()>0;
	}
    
	public Long getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(Long relationshipId) {
		this.relationshipId = relationshipId;
	}

	public Person getPersonTo() {
		return personTo;
	}

	public void setPersonTo(Person personTo) {
		this.personTo = personTo;
	}

	public Person getPersonFrom() {
		return personFrom;
	}

	public void setPersonFrom(Person personFrom) {
		this.personFrom = personFrom;
	}

	public Boolean getDirected() {
		return directed;
	}

	public void setDirected(Boolean directed) {
		this.directed = directed;
	}
	
	public String getRoleTo() {
		return roleTo;
	}

	public void setRoleTo(String roleTo) {
		this.roleTo = roleTo;
	}

	public String getRoleFrom() {
		return roleFrom;
	}

	public void setRoleFrom(String roleFrom) {
		this.roleFrom = roleFrom;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public Boolean getToIsCurator() {
		return toIsCurator;
	}

	public void setToIsCurator(Boolean toIsCurator) {
		this.toIsCurator = toIsCurator;
	}

	public Boolean getFromIsCurator() {
		return fromIsCurator;
	}

	public void setFromIsCurator(Boolean fromIsCurator) {
		this.fromIsCurator = fromIsCurator;
	}

	public Long getPersonToId() {
		return personToId;
	}

	public void setPersonToId(Long personToId) {
		this.personToId = personToId;
	}

	public Long getPersonFromId() {
		return personFromId;
	}

	public void setPersonFromId(Long personFromId) {
		this.personFromId = personFromId;
	}
}
