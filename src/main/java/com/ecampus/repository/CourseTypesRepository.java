package com.ecampus.repository;

import com.ecampus.model.CourseTypes;
import com.ecampus.model.CourseTypesId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTypesRepository extends JpaRepository<CourseTypes, CourseTypesId> {

//    // Find all course requirements for a given scheme
//    List<CourseRequirement> findBySchemeId(Long schemeId);
//
//    // Optionally: Find by scheme + course type
//    CourseRequirement findBySchemeIdAndCourseTypeCode(Long schemeId, String courseTypeCode);
//
//    List<CourseRequirement> findByProgramIdAndSchemeId(Long programId, Long schemeId);
}
