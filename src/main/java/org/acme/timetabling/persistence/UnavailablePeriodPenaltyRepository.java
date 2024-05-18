package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.UnavailablePeriodPenalty;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UnavailablePeriodPenaltyRepository extends PagingAndSortingRepository<UnavailablePeriodPenalty, Long> {
    @Override
    List<UnavailablePeriodPenalty> findAll();
}
