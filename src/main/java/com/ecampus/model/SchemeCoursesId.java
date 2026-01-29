package com.ecampus.model;

import java.io.Serializable;
import java.util.Objects;

public class SchemeCoursesId implements Serializable {

    private Long semNo;
    private Long schemeId;
    private Long courseSrNo;

    public SchemeCoursesId() {}

    public SchemeCoursesId(Long semNo, Long schemeId, Long courseSrNo) {
        this.semNo = semNo;
        this.schemeId = schemeId;
        this.courseSrNo = courseSrNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchemeCoursesId that)) return false;
        return Objects.equals(semNo, that.semNo) &&
                Objects.equals(schemeId, that.schemeId) &&
                Objects.equals(courseSrNo, that.courseSrNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(semNo, schemeId, courseSrNo);
    }
}
