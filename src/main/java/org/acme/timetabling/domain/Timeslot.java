package org.acme.timetabling.domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Timeslot {

    private static final String[] TIMES = { "08:45-10:20", "10:30-12:05", "12:35-14:10", "14:20-15:55", "16:15-17:50", "18:00-19:35", "19:45-21:20", "20:30-23:05" };

    @PlanningId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int timeslotIndex;

    public Timeslot() {
    }

    public Timeslot(int timeslotIndex) {
        this.timeslotIndex = timeslotIndex;
    }

    public Long getId() {
        return id;
    }

    public int getTimeslotIndex() {
        return timeslotIndex;
    }

    public String getLabel() {
        String time = TIMES[timeslotIndex % TIMES.length];
        if (timeslotIndex > TIMES.length) {
            return "Timeslot " + timeslotIndex;
        }
        return time;
    }

    @Override
    public String toString() {
        return Integer.toString(timeslotIndex);
    }
}
