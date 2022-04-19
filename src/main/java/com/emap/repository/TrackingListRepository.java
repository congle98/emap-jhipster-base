package com.emap.repository;

import com.emap.domain.TrackingList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TrackingList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackingListRepository extends JpaRepository<TrackingList, Long> {}
