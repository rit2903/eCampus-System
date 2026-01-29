package com.ecampus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "schemecourses")
@IdClass(SchemeCoursesId.class)
public class SchemeCourses {

    @Id
    @Column(name = "sem_no")
    private Long semNo;

    @Id
    @Column(name = "scheme_id")
    private Long schemeId;

    @Id
    @Column(name = "course_sr_no")
    private Long courseSrNo;

    @Column(name = "course_type_code")
    private String courseTypeCode;

    @Column(name = "course_id")
    private Long courseId;
    @Column(name = "crsid")
    private Long crsid;
    @Column(name = "course_code") // length = 5
    private String courseCode;

    @Column(name = "course_title")
    private String courseTitle;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "lecture_hours")
    private Long lectureHours;

    @Column(name = "tutorial_hours")
    private Long tutorialHours;

    @Column(name = "practical_hours")
    private Long practicalHours;

    @Column(name = "total_credits")
    private Long totalCredits;

    // Getters and Setters

    public Long getSemNo() {
        return semNo;
    }

    public void setSemNo(Long semNo) {
        this.semNo = semNo;
    }

    public Long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    public Long getCourseSrNo() {
        return courseSrNo;
    }

    public void setCourseSrNo(Long courseSrNo) {
        this.courseSrNo = courseSrNo;
    }

    public String getCourseTypeCode() {
        return courseTypeCode;
    }

    public void setCourseTypeCode(String courseTypeCode) {
        this.courseTypeCode = courseTypeCode;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getLectureHours() {
        return lectureHours;
    }

    public void setLectureHours(Long lectureHours) {
        this.lectureHours = lectureHours;
    }

    public Long getTutorialHours() {
        return tutorialHours;
    }

    public void setTutorialHours(Long tutorialHours) {
        this.tutorialHours = tutorialHours;
    }

    public Long getPracticalHours() {
        return practicalHours;
    }

    public void setPracticalHours(Long practicalHours) {
        this.practicalHours = practicalHours;
    }

    public Long getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Long totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Long getCrsid() { return crsid; }
    public void setCrsid(Long crsid) { this.crsid = crsid; }

}
