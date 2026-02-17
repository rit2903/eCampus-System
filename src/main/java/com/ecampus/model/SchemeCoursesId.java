package com.ecampus.model;

import java.io.Serializable;
import java.util.Objects;

public class SchemeCoursesId implements Serializable {

    private Long schemeId;
    private Long splid;
    private Long semNo;
    private Long courseSrNo;

    public SchemeCoursesId() {}

    public SchemeCoursesId(Long schemeId, Long splid, Long semNo, Long courseSrNo) {
        this.schemeId = schemeId;
        this.splid = splid;
        this.semNo = semNo;
        this.courseSrNo = courseSrNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchemeCoursesId that)) return false;
        return Objects.equals(schemeId, that.schemeId)
                && Objects.equals(splid, that.splid)
                && Objects.equals(semNo, that.semNo)
                && Objects.equals(courseSrNo, that.courseSrNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemeId, splid, semNo, courseSrNo);
    }
}
