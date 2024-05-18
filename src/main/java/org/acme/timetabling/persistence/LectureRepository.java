package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Lecture;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LectureRepository extends PagingAndSortingRepository<Lecture, Long> {
    @Override
    List<Lecture> findAll();
}
