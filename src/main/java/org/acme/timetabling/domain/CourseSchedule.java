package org.acme.timetabling.domain;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.acme.timetabling.domain.solver.CourseConflict;
import org.optaplanner.core.api.solver.SolverStatus;

@PlanningSolution
public class CourseSchedule {

    @ProblemFactCollectionProperty
    private List<Teacher> teacherList;

    @ProblemFactCollectionProperty
    private List<Curriculum> curriculumList;

    @ProblemFactCollectionProperty
    private List<Course> courseList;

    @ProblemFactCollectionProperty
    private List<Weekday> weekdayList;

    @ProblemFactCollectionProperty
    private List<Timeslot> timeslotList;

    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Period> periodList;

    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Room> roomList;

    @ProblemFactCollectionProperty
    private List<UnavailablePeriodPenalty> unavailablePeriodPenaltyList;

    @PlanningEntityCollectionProperty
    private List<Lecture> lectureList;

    @PlanningScore
    private HardSoftScore score;

    private SolverStatus solverStatus;

    public CourseSchedule() {
    }

    public CourseSchedule(List<Teacher> teacherList, List<Curriculum> curriculumList, List<Course> courseList,
                          List<Weekday> weekdayList, List<Timeslot> timeslotList, List<Period> periodList, List<Room> roomList,
                          List<UnavailablePeriodPenalty> unavailablePeriodPenaltyList, List<Lecture> lectureList) {
        this.teacherList = teacherList;
        this.curriculumList = curriculumList;
        this.courseList = courseList;
        this.weekdayList = weekdayList;
        this.timeslotList = timeslotList;
        this.periodList = periodList;
        this.roomList = roomList;
        this.unavailablePeriodPenaltyList = unavailablePeriodPenaltyList;
        this.lectureList = lectureList;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public List<Curriculum> getCurriculumList() {
        return curriculumList;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public List<Weekday> getWeekdayList() {
        return weekdayList;
    }

    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public List<UnavailablePeriodPenalty> getUnavailablePeriodPenaltyList() {
        return unavailablePeriodPenaltyList;
    }

    public List<Lecture> getLectureList() {
        return lectureList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public SolverStatus getSolverStatus() {
        return solverStatus;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }

    @ProblemFactCollectionProperty
    private List<CourseConflict> calculateCourseConflictList() {
        List<CourseConflict> courseConflictList = new ArrayList<>();
        for (Course leftCourse : courseList) {
            for (Course rightCourse : courseList) {
                if (leftCourse.getId() < rightCourse.getId()) {
                    int conflictCount = 0;
                    if (leftCourse.getTeacher().equals(rightCourse.getTeacher())) {
                        conflictCount++;
                    }
                    for (Curriculum curriculum : leftCourse.getCurriculumSet()) {
                        if (rightCourse.getCurriculumSet().contains(curriculum)) {
                            conflictCount++;
                        }
                    }
                    if (conflictCount > 0) {
                        courseConflictList.add(new CourseConflict(leftCourse, rightCourse, conflictCount));
                    }
                }
            }
        }
        return courseConflictList;
    }
}
