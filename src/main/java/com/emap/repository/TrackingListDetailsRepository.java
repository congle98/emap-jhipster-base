package com.emap.repository;

import com.emap.domain.TrackingListDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TrackingListDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackingListDetailsRepository extends JpaRepository<TrackingListDetails, Long> {}
