package com.emap.repository;

import com.emap.domain.ConfigSetting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ConfigSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigSettingRepository extends JpaRepository<ConfigSetting, Long> {}
