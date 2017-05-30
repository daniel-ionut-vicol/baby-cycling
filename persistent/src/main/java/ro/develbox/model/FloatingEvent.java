package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the floating_event database table.
 * 
 */
@Entity
@Table(name = "floating_event")
@NamedQuery(name = "FloatingEvent.findAll", query = "SELECT f FROM FloatingEvent f")
public class FloatingEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STOP_DATE")
    private Date stopDate;

    // bi-directional many-to-one association to Event
    @ManyToOne
    @JoinColumn(name = "EVENT")
    private Event eventBean;

    public FloatingEvent() {
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return this.stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public Event getEventBean() {
        return this.eventBean;
    }

    public void setEventBean(Event eventBean) {
        this.eventBean = eventBean;
    }

}