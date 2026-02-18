package com.ecampus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "schemecourses", schema="ec2")
@IdClass(SchemeCoursesId.class)
public class SchemeCourses {

    // ---- PRIMARY KEY ----

    @Id
    @Column(name = "scheme_id")
    private Long schemeId;

    @Id
    @Column(name = "splid")
    private Long splid;

    @Id
    @Column(name = "term_name")
    private String termName;

    @Id
    @Column(name = "program_year")
    private Long programYear;

    @Id
    @Column(name = "course_sr_no")
    private Long courseSrNo;

    // ---- NON-PK ----

    @Column(name = "sem_no")
    private Long semNo;

    @Column(name = "semester_name")
    private String semesterName;

    @Column(name = "term_seq_no")
    private Long termSeqNo;

    // ---- FOREIGN KEY TO CourseTypes ----

    @Column(name = "ctpcode")
    private String ctpcode;

    // ---- COURSE INFO ----

    @Column(name = "crsid")
    private Long crsid;

    @Column(name = "course_code") // length = 5
    private String courseCode;

    @Column(name = "course_title")
    private String courseTitle;

    @Column(name = "lecture_hours")
    private Long lectureHours;

    @Column(name = "tutorial_hours")
    private Long tutorialHours;

    @Column(name = "practical_hours")
    private Long practicalHours;

    @Column(name = "total_credits")
    private Long totalCredits;

    // ---- getters & setters ----

    public Long getSchemeId() {
        return schemeId;
    }
    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    public Long getSplid() { return splid; }
    public void setSplid(Long splid) { this.splid = splid; }

    public String getTermName() { return termName; }
    public void setTermName(String termName) { this.termName = termName; }

    public Long getProgramYear() { return programYear; }
    public void setProgramYear(Long programYear) { this.programYear = programYear; }

    public Long getCourseSrNo() {
        return courseSrNo;
    }
    public void setCourseSrNo(Long courseSrNo) {
        this.courseSrNo = courseSrNo;
    }

    public Long getSemNo() {
        return semNo;
    }
    public void setSemNo(Long semNo) {
        this.semNo = semNo;
    }

    public String getSemesterName() { return semesterName; }
    public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

    public Long getTermSeqNo() { return termSeqNo; }
    public void setTermSeqNo(Long termSeqNo) { this.termSeqNo = termSeqNo; }

    public String getCtpcode() { return ctpcode; }
    public void setCtpcode(String ctpcode) { this.ctpcode = ctpcode; }

    public Long getCrsid() { return crsid; }
    public void setCrsid(Long crsid) { this.crsid = crsid; }

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
}
