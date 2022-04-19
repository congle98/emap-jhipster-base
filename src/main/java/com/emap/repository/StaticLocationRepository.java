package com.emap.repository;

import com.emap.domain.StaticLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StaticLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StaticLocationRepository extends JpaRepository<StaticLocation, Long> {}
