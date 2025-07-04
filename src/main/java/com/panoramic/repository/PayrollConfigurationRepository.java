package com.panoramic.repository;

import com.panoramic.model.PayrollConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollConfigurationRepository extends JpaRepository<PayrollConfiguration, Long> {
    Optional<PayrollConfiguration> findByConfigNameAndIsActive(String configName, Boolean isActive);
    Optional<PayrollConfiguration> findByIsActive(Boolean isActive);
}
