package org.acme.timetabling.domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class UnavailablePeriodPenalty {

    @PlanningId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Period period;

    public UnavailablePeriodPenalty() {
    }

    public UnavailablePeriodPenalty(Course course, Period period) {
        this.course = requireNonNull(course);
        this.period = requireNonNull(period);
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return course + "@" + period;
    }
}
