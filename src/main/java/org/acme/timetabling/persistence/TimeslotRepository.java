package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Timeslot;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TimeslotRepository extends PagingAndSortingRepository<Timeslot, Long> {
    @Override
    List<Timeslot> findAll();
}
