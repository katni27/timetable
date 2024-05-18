package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Period;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PeriodRepository extends PagingAndSortingRepository<Period, Long> {
    @Override
    List<Period> findAll();
}
