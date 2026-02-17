package com.ecampus.model;

import java.io.Serializable;
import java.util.Objects;

public class CourseTypesId implements Serializable {

    private Long schemeId;
    private Long splid;
    private String ctpcode;

    public CourseTypesId() {}

    public CourseTypesId(Long schemeId, Long splid, String ctpcode) {
        this.schemeId = schemeId;
        this.splid = splid;
        this.ctpcode = ctpcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseTypesId that)) return false;
        return Objects.equals(schemeId, that.schemeId)
                && Objects.equals(splid, that.splid)
                && Objects.equals(ctpcode, that.ctpcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemeId, splid, ctpcode);
    }
}
