package com.ecampus.repository;

import com.ecampus.model.SchemeCourses;
import com.ecampus.model.SchemeCoursesId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchemeCoursesRepository extends JpaRepository<SchemeCourses, SchemeCoursesId> {

    // Get all semester courses for a given scheme
    List<SchemeCourses> findBySchemeId(Long schemeId);

    // Find a single course by composite key
    SchemeCourses findBySchemeIdAndSemNoAndCourseSrNo(Long schemeId, Long semNo, Long courseSrNo);

    // Optional: get by scheme + semester number
    List<SchemeCourses> findBySchemeIdAndSemNo(Long schemeId, Long semNo);
    List<SchemeCourses> findBySchemeIdAndSemNoOrderByCourseSrNo(Long schemeId, Long semNo);
}
