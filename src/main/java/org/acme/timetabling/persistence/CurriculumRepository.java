package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Curriculum;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CurriculumRepository extends PagingAndSortingRepository<Curriculum, Long> {
    @Override
    List<Curriculum> findAll();
}
