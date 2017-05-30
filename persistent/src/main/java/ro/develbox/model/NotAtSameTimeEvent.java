package ro.develbox.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the not_at_same_time_event database table.
 * 
 */
@Entity
@Table(name = "not_at_same_time_event")
@NamedQuery(name = "NotAtSameTimeEvent.findAll", query = "SELECT n FROM NotAtSameTimeEvent n")
public class NotAtSameTimeEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "base_start_date")
    private Date baseStartDate;

    @Column(name = "DELAY_MINUTES")
    private int delayMinutes;

    // bi-directional many-to-one association to Event
    @ManyToOne
    @JoinColumn(name = "EVENT")
    private Event eventBean;

    public NotAtSameTimeEvent() {
    }

    public Date getBaseStartDate() {
        return this.baseStartDate;
    }

    public void setBaseStartDate(Date baseStartDate) {
        this.baseStartDate = baseStartDate;
    }

    public int getDelayMinutes() {
        return this.delayMinutes;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public Event getEventBean() {
        return this.eventBean;
    }

    public void setEventBean(Event eventBean) {
        this.eventBean = eventBean;
    }

}