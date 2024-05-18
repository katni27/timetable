package org.acme.timetabling.persistence;

import java.util.List;

import org.acme.timetabling.domain.Room;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoomRepository extends PagingAndSortingRepository<Room, Long> {
    @Override
    List<Room> findAll();
}
