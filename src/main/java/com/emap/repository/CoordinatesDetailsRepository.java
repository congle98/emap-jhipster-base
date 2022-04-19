package com.emap.repository;

import com.emap.domain.CoordinatesDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CoordinatesDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoordinatesDetailsRepository extends JpaRepository<CoordinatesDetails, Long> {}
