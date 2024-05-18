package org.acme.timetabling.domain;

import static java.util.Objects.requireNonNull;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Teacher {

    @PlanningId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    public Teacher() {
    }

    public Teacher(String code) {
        this.code = requireNonNull(code);
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
