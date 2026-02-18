package com.ecampus.repository;

import com.ecampus.model.CourseTypes;
import com.ecampus.model.CourseTypesId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTypesRepository extends JpaRepository<CourseTypes, CourseTypesId> {
    // Find all course types for a scheme (all specializations)
    List<CourseTypes> findBySchemeId(Long schemeId);

    // Find course types for a scheme restricted to specific splid values
    List<CourseTypes> findBySchemeIdAndSplidIn(Long schemeId, java.util.List<Long> splids);

//    // Legacy notes / examples
//    // List<CourseRequirement> findBySchemeId(Long schemeId);
//    // CourseRequirement findBySchemeIdAndCourseTypeCode(Long schemeId, String courseTypeCode);
//    // List<CourseRequirement> findByProgramIdAndSchemeId(Long programId, Long schemeId);
}
