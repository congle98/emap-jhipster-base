package com.emap.repository;

import com.emap.domain.WarningMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WarningMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarningMessageRepository extends JpaRepository<WarningMessage, Long> {}
