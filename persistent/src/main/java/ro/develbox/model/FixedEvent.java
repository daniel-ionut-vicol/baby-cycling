package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the fixed_event database table.
 * 
 */
@Entity
@Table(name="fixed_event")
@NamedQuery(name="FixedEvent.findAll", query="SELECT f FROM FixedEvent f")
public class FixedEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_date")
	private Date startDate;

	//bi-directional many-to-one association to Event
	@ManyToOne
	@JoinColumn(name="EVENT")
	private Event eventBean;

	public FixedEvent() {
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Event getEventBean() {
		return this.eventBean;
	}

	public void setEventBean(Event eventBean) {
		this.eventBean = eventBean;
	}

}