package com.emap.repository;

import com.emap.domain.WarningRule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WarningRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarningRuleRepository extends JpaRepository<WarningRule, Long> {}
