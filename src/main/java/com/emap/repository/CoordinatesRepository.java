package com.emap.repository;

import com.emap.domain.Coordinates;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Coordinates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {}
