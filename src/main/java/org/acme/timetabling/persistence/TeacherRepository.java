package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Teacher;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {
    @Override
    List<Teacher> findAll();
}
