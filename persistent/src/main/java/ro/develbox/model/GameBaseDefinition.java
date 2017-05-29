package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the game_base_definition database table.
 * 
 */
@Entity
@Table(name="game_base_definition")
@NamedQuery(name="GameBaseDefinition.findAll", query="SELECT g FROM GameBaseDefinition g")
public class GameBaseDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String name;

	//bi-directional many-to-one association to CheckpointGameDefinition
	@OneToMany(mappedBy="gameBaseDefinition")
	private List<CheckpointGameDefinition> checkpointGameDefinitions;

	//bi-directional many-to-one association to Event
	@OneToMany(mappedBy="gameBaseDefinition")
	private List<Event> events;

	//bi-directional many-to-one association to Game
	@ManyToOne
	@JoinColumn(name="TYPE")
	private Game game;

	//bi-directional many-to-many association to Group
	@ManyToMany
	@JoinTable(
		name="grops_maps"
		, joinColumns={
			@JoinColumn(name="MAP")
			}
		, inverseJoinColumns={
			@JoinColumn(name="GROP")
			}
		)
	private List<Group> groups;

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="gameBaseDefinitions")
	private List<User> users;

	public GameBaseDefinition() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CheckpointGameDefinition> getCheckpointGameDefinitions() {
		return this.checkpointGameDefinitions;
	}

	public void setCheckpointGameDefinitions(List<CheckpointGameDefinition> checkpointGameDefinitions) {
		this.checkpointGameDefinitions = checkpointGameDefinitions;
	}

	public CheckpointGameDefinition addCheckpointGameDefinition(CheckpointGameDefinition checkpointGameDefinition) {
		getCheckpointGameDefinitions().add(checkpointGameDefinition);
		checkpointGameDefinition.setGameBaseDefinition(this);

		return checkpointGameDefinition;
	}

	public CheckpointGameDefinition removeCheckpointGameDefinition(CheckpointGameDefinition checkpointGameDefinition) {
		getCheckpointGameDefinitions().remove(checkpointGameDefinition);
		checkpointGameDefinition.setGameBaseDefinition(null);

		return checkpointGameDefinition;
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Event addEvent(Event event) {
		getEvents().add(event);
		event.setGameBaseDefinition(this);

		return event;
	}

	public Event removeEvent(Event event) {
		getEvents().remove(event);
		event.setGameBaseDefinition(null);

		return event;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}