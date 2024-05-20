package org.acme.timetabling;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.acme.timetabling.domain.*;
import org.acme.timetabling.persistence.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourseScheduleSpringBootApp {

    private static final int WEEKDAY_LIST_SIZE = 5;
    private static final int TIMESLOT_LIST_SIZE = 4;

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

            for (int i = 0; i < WEEKDAY_LIST_SIZE; i++) {
                weekdayRepository.save(new Weekday(i, new ArrayList<>(TIMESLOT_LIST_SIZE)));
            }

            for (int i = 0; i < TIMESLOT_LIST_SIZE; i++) {
                timeslotRepository.save(new Timeslot(i));
            }

            for (Weekday weekday : weekdayRepository.findAll()) {
                for (Timeslot timeslot : timeslotRepository.findAll()) {
                    periodRepository.save(new Period(weekday, timeslot));
                }
            }

            String filePath = "src/main/resources/Load.json";
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                JSONObject jsonObject = new JSONObject(content);

                JSONArray teacherList = jsonObject.getJSONArray("teacherList");
                for (int i = 0; i < teacherList.length(); i++) {
                    JSONObject teacher = teacherList.getJSONObject(i);
                    teacherRepository.save(new Teacher(teacher.getInt("id") + 1L, teacher.getString("code")));
                }

                JSONArray curriculumList = jsonObject.getJSONArray("curriculumList");
                for (int i = 0; i < curriculumList.length(); i++) {
                    JSONObject curriculum = curriculumList.getJSONObject(i);
                    curriculumRepository.save(new Curriculum(curriculum.getInt("id") + 1L, curriculum.getString("code")));
                }

                JSONArray courseList = jsonObject.getJSONArray("courseList");
                for (int i = 0; i < courseList.length(); i++) {
                    JSONObject course = courseList.getJSONObject(i);

                    Teacher teacher = teacherRepository.findById(course.getInt("teacher") + 1L).orElseThrow();
                    String code = course.getString("code");
                    Long id = course.getInt("id") + 1L;
                    int lectureSize = course.getInt("lectureSize");
                    int minWorkingDaySize = course.getInt("minWorkingDaySize");
                    int studentSize = course.getInt("studentSize");

                    JSONArray curriculumSet = course.getJSONArray("curriculumSet");
                    Curriculum[] set = new Curriculum[curriculumSet.length()];
                    for (int j = 0; j < curriculumSet.length(); j++) {
                        set[j] = curriculumRepository.findById(curriculumSet.getInt(j) + 1L).orElseThrow();
                    }

                    courseRepository.save(new Course(
                            id,
                            code,
                            teacher,
                            lectureSize,
                            studentSize,
                            minWorkingDaySize,
                            set
                    ));
                }

                List<String> roomTypeList = new ArrayList<>();
                JSONArray typeList = jsonObject.getJSONArray("roomTypeList");
                for (int i = 0; i < typeList.length(); i++) {
                    JSONObject type = typeList.getJSONObject(i);
                    roomTypeList.add(type.getString("code"));
                }

                JSONArray roomList = jsonObject.getJSONArray("roomList");
                for (int i = 0; i < roomList.length(); i++) {
                    JSONObject room = roomList.getJSONObject(i);

                    int capacity = room.getInt("capacity");

                    String type = roomTypeList.get(room.getInt("type") % roomTypeList.size());
                    String code = room.getString("code");

                    roomRepository.save(new Room(
                            code,
                            type,
                            capacity
                    ));
                }

                JSONArray lecturesList = jsonObject.getJSONArray("lecturesList");
                for (int i = 0; i < lecturesList.length(); i++) {
                    JSONObject lecture = lecturesList.getJSONObject(i);

                    int lectureIndexInCourse = lecture.getInt("lectureIndexInCourse") + 1;

                    Course course = courseRepository.findById(lecture.getInt("course") + 1L).orElseThrow();

                    lectureRepository.save(new Lecture(
                            course,
                            lectureIndexInCourse,
                            false
                    ));
                }

                Random random = new Random(37);
                int periodRepositorySize = periodRepository.findAll().size() - 1;
                for (Course course : courseRepository.findAll()) {
                    Period period = periodRepository.findById(random.nextInt(periodRepositorySize) + 1L).orElseThrow();
                    unavailablePeriodPenaltyRepository.save(new UnavailablePeriodPenalty(
                            course,
                            period
                    ));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
