package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the event database table.
 * 
 */
@Entity
@NamedQuery(name="Event.findAll", query="SELECT e FROM Event e")
public class Event implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String name;

	@Column(name="PUBLIC")
	private Object public_;

	//bi-directional many-to-one association to GameBaseDefinition
	@ManyToOne
	@JoinColumn(name="GAME_ID")
	private GameBaseDefinition gameBaseDefinition;

	//bi-directional many-to-one association to EventParticipant
	@OneToMany(mappedBy="eventBean")
	private List<EventParticipant> eventParticipants;

	//bi-directional many-to-one association to FixedEvent
	@OneToMany(mappedBy="eventBean")
	private List<FixedEvent> fixedEvents;

	//bi-directional many-to-one association to FloatingEvent
	@OneToMany(mappedBy="eventBean")
	private List<FloatingEvent> floatingEvents;

	//bi-directional many-to-one association to NotAtSameTimeEvent
	@OneToMany(mappedBy="eventBean")
	private List<NotAtSameTimeEvent> notAtSameTimeEvents;

	public Event() {
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

	public Object getPublic_() {
		return this.public_;
	}

	public void setPublic_(Object public_) {
		this.public_ = public_;
	}

	public GameBaseDefinition getGameBaseDefinition() {
		return this.gameBaseDefinition;
	}

	public void setGameBaseDefinition(GameBaseDefinition gameBaseDefinition) {
		this.gameBaseDefinition = gameBaseDefinition;
	}

	public List<EventParticipant> getEventParticipants() {
		return this.eventParticipants;
	}

	public void setEventParticipants(List<EventParticipant> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public EventParticipant addEventParticipant(EventParticipant eventParticipant) {
		getEventParticipants().add(eventParticipant);
		eventParticipant.setEventBean(this);

		return eventParticipant;
	}

	public EventParticipant removeEventParticipant(EventParticipant eventParticipant) {
		getEventParticipants().remove(eventParticipant);
		eventParticipant.setEventBean(null);

		return eventParticipant;
	}

	public List<FixedEvent> getFixedEvents() {
		return this.fixedEvents;
	}

	public void setFixedEvents(List<FixedEvent> fixedEvents) {
		this.fixedEvents = fixedEvents;
	}

	public FixedEvent addFixedEvent(FixedEvent fixedEvent) {
		getFixedEvents().add(fixedEvent);
		fixedEvent.setEventBean(this);

		return fixedEvent;
	}

	public FixedEvent removeFixedEvent(FixedEvent fixedEvent) {
		getFixedEvents().remove(fixedEvent);
		fixedEvent.setEventBean(null);

		return fixedEvent;
	}

	public List<FloatingEvent> getFloatingEvents() {
		return this.floatingEvents;
	}

	public void setFloatingEvents(List<FloatingEvent> floatingEvents) {
		this.floatingEvents = floatingEvents;
	}

	public FloatingEvent addFloatingEvent(FloatingEvent floatingEvent) {
		getFloatingEvents().add(floatingEvent);
		floatingEvent.setEventBean(this);

		return floatingEvent;
	}

	public FloatingEvent removeFloatingEvent(FloatingEvent floatingEvent) {
		getFloatingEvents().remove(floatingEvent);
		floatingEvent.setEventBean(null);

		return floatingEvent;
	}

	public List<NotAtSameTimeEvent> getNotAtSameTimeEvents() {
		return this.notAtSameTimeEvents;
	}

	public void setNotAtSameTimeEvents(List<NotAtSameTimeEvent> notAtSameTimeEvents) {
		this.notAtSameTimeEvents = notAtSameTimeEvents;
	}

	public NotAtSameTimeEvent addNotAtSameTimeEvent(NotAtSameTimeEvent notAtSameTimeEvent) {
		getNotAtSameTimeEvents().add(notAtSameTimeEvent);
		notAtSameTimeEvent.setEventBean(this);

		return notAtSameTimeEvent;
	}

	public NotAtSameTimeEvent removeNotAtSameTimeEvent(NotAtSameTimeEvent notAtSameTimeEvent) {
		getNotAtSameTimeEvents().remove(notAtSameTimeEvent);
		notAtSameTimeEvent.setEventBean(null);

		return notAtSameTimeEvent;
	}

}