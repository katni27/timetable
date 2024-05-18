package org.acme.timetabling.domain;

import java.util.Set;

import javax.persistence.*;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.entity.PlanningPin;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.acme.timetabling.domain.solver.LectureDifficultyWeightFactory;
import org.acme.timetabling.domain.solver.PeriodStrengthWeightFactory;
import org.acme.timetabling.domain.solver.RoomStrengthWeightFactory;

@PlanningEntity(difficultyWeightFactoryClass = LectureDifficultyWeightFactory.class)
@Entity
public class Lecture {

    @PlanningId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Course course;
    private int lectureIndexInCourse;
    private boolean pinned;

    @PlanningVariable(strengthWeightFactoryClass = PeriodStrengthWeightFactory.class)
    @ManyToOne
    private Period period;

    @PlanningVariable(strengthWeightFactoryClass = RoomStrengthWeightFactory.class)
    @ManyToOne
    private Room room;

    public Lecture() {
    }

    public Lecture(Course course, int lectureIndexInCourse, boolean pinned) {
        this.course = course;
        this.lectureIndexInCourse = lectureIndexInCourse;
        this.pinned = pinned;
    }

    public Lecture(long id, Course course, int lectureIndexInCourse, boolean pinned, Period period, Room room) {
        this(course, lectureIndexInCourse, pinned);
        this.id = id;
        this.period = period;
        this.room = room;
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

    public int getLectureIndexInCourse() {
        return lectureIndexInCourse;
    }

    @PlanningPin
    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Teacher getTeacher() {
        return course.getTeacher();
    }

    public int getStudentSize() {
        return course.getStudentSize();
    }

    public Set<Curriculum> getCurriculumSet() {
        return course.getCurriculumSet();
    }

    public Weekday getWeekDay() {
        if (period == null) {
            return null;
        }
        return period.getWeekDay();
    }

    public int getTimeslotIndex() {
        if (period == null) {
            return Integer.MIN_VALUE;
        }
        return period.getTimeslot().getTimeslotIndex();
    }

    public String getLabel() {
        return course.getCode() + "-" + lectureIndexInCourse;
    }

    @Override
    public String toString() {
        return course + "-" + lectureIndexInCourse;
    }
}
