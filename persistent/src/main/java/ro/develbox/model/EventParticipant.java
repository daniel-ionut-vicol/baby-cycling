package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the event_participants database table.
 * 
 */
@Entity
@Table(name="event_participants")
@NamedQuery(name="EventParticipant.findAll", query="SELECT e FROM EventParticipant e")
public class EventParticipant implements Serializable {
	private static final long serialVersionUID = 1L;

	private Object confirmed;

	//bi-directional many-to-one association to Event
	@ManyToOne
	@JoinColumn(name="EVENT")
	private Event eventBean;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER")
	private User userBean;

	public EventParticipant() {
	}

	public Object getConfirmed() {
		return this.confirmed;
	}

	public void setConfirmed(Object confirmed) {
		this.confirmed = confirmed;
	}

	public Event getEventBean() {
		return this.eventBean;
	}

	public void setEventBean(Event eventBean) {
		this.eventBean = eventBean;
	}

	public User getUserBean() {
		return this.userBean;
	}

	public void setUserBean(User userBean) {
		this.userBean = userBean;
	}

}