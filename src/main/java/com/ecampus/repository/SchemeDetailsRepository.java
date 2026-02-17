package com.ecampus.repository;

import com.ecampus.model.SchemeDetails;
import com.ecampus.model.SchemeDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchemeDetailsRepository extends JpaRepository<SchemeDetails, SchemeDetailsId> {
    List<SchemeDetails> findBySchemeId(Long schemeId);
    Optional<SchemeDetails> findBySchemeIdAndSplid(Long schemeId, Long splid);
}
