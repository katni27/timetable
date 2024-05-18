package org.acme.timetabling;

import org.acme.timetabling.domain.*;
import org.acme.timetabling.persistence.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourseScheduleSpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(CourseScheduleSpringBootApp.class, args);
    }

    @Bean
    public CommandLineRunner demoData(
            TeacherRepository teacherRepository,
            CurriculumRepository curriculumRepository,
            CourseRepository courseRepository,
            WeekdayRepository weekdayRepository,
            TimeslotRepository timeslotRepository,
            PeriodRepository periodRepository,
            RoomRepository roomRepository,
            UnavailablePeriodPenaltyRepository unavailablePeriodPenaltyRepository,
            LectureRepository lectureRepository) {
        return (args) -> {
            teacherRepository.save(new Teacher("Алексей Михайлович"));
            curriculumRepository.save(new Curriculum("ФСЭиП-201"));

            Curriculum curriculum = curriculumRepository.findById(1L).orElseThrow();
            Teacher teacher = teacherRepository.findById(1L).orElseThrow();

            Curriculum[] set = new Curriculum[1];
            set[0] = curriculum;

            courseRepository.save(new Course(
                    "Метод конечных элементов для решения задач в строительстве(практ.зан.  и семин.)",
                    teacher,
                    8,
                    11,
                    16,
                    set
            ));

            roomRepository.save(new Room(
                    "317/1",
                    "Учебная аудитория",
                    24
            ));

            lectureRepository.save(new Lecture(
                    courseRepository.findById(1L).orElseThrow(),
                    0,
                    false
            ));
        };
    }
}
