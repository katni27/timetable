package org.acme.timetabling.domain;

import static java.util.Objects.requireNonNull;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import javax.persistence.*;

@Entity
public class Period {

    @PlanningId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Weekday weekday;

    @ManyToOne
    private Timeslot timeslot;

    public Period() {
    }

    public Period(Weekday weekday, Timeslot timeslot) {
        this.weekday = requireNonNull(weekday);
        weekday.getPeriodList().add(this);
        this.timeslot = requireNonNull(timeslot);
    }

    public Long getId() {
        return id;
    }

    public Weekday getWeekDay() {
        return weekday;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public String getLabel() {
        return weekday.getLabel() + " " + timeslot.getLabel();
    }

    @Override
    public String toString() {
        return weekday + "-" + timeslot;
    }
}
