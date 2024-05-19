package org.acme.timetabling.domain;

import java.util.Objects;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Room {

    @PlanningId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String type;
    private int capacity;

    public Room() {
    }

    public Room(String code, String type, int capacity) {
        this.code = Objects.requireNonNull(code);
        this.type = type;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public  String getType() {
        return this.type;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return code;
    }
}
