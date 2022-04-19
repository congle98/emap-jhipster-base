package com.emap.repository;

import com.emap.domain.Target;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Target entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {}
