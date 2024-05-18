package org.acme.timetabling.persistence;

import org.acme.timetabling.domain.CourseSchedule;
import org.acme.timetabling.domain.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseScheduleRepository {
    public static final Long SINGLETON_TIME_TABLE_ID = 1L;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CurriculumRepository curriculumRepository;
    @Autowired
    private WeekdayRepository weekdayRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TimeslotRepository timeslotRepository;
    @Autowired
    private UnavailablePeriodPenaltyRepository unavailablePeriodPenaltyRepository;

    public CourseSchedule findById(Long id) {
        if (!SINGLETON_TIME_TABLE_ID.equals(id)) {
            throw new IllegalStateException("There is no timeTable with id (" + id + ").");
        }
        // Occurs in a single transaction, so each initialized lesson references the same timeslot/room instance
        // that is contained by the timeTable's timeslotList/roomList.
        return new CourseSchedule(
                teacherRepository.findAll(),
                curriculumRepository.findAll(),
                courseRepository.findAll(),
                weekdayRepository.findAll(),
                timeslotRepository.findAll(),
                periodRepository.findAll(),
                roomRepository.findAll(),
                unavailablePeriodPenaltyRepository.findAll(),
                lectureRepository.findAll()
        );
    }

    public void save(CourseSchedule timeTable) {
        for (Lecture lecture : timeTable.getLectureList()) {
            // TODO this is awfully naive: optimistic locking causes issues if called by the SolverManager
            lectureRepository.findById(lecture.getId()).ifPresent(attachedLesson -> {
                attachedLesson.setPeriod(lecture.getPeriod());
                attachedLesson.setRoom(lecture.getRoom());
            });
        }
    }
}
