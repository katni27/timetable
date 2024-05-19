package org.acme.timetabling.domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

@Entity
public class Weekday {

    private static final String[] WEEKDAYS = { "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс" };

    @PlanningId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int dayIndex;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Period> periodList;

    public Weekday() {
    }

    public Weekday(int dayIndex, List<Period> periods) {
        this.dayIndex = dayIndex;
        this.periodList = periods;
    }

    public Long getId() {
        return id;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public String getLabel() {
        String weekday = WEEKDAYS[dayIndex % WEEKDAYS.length];
        if (dayIndex > WEEKDAYS.length) {
            return "Day " + dayIndex;
        }
        return weekday;
    }

    @Override
    public String toString() {
        return Integer.toString(dayIndex);
    }

}
