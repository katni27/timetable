package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Weekday;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WeekdayRepository extends PagingAndSortingRepository<Weekday, Long> {
    @Override
    List<Weekday> findAll();
}
