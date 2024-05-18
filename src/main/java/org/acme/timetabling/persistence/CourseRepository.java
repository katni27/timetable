package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Course;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
    @Override
    List<Course> findAll();
}
