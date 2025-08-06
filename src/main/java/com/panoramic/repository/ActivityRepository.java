package com.panoramic.repository;

import com.panoramic.model.Activity;
import com.panoramic.model.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Optional filtering methods
    Page<Activity> findByEntityTypeOrderByCreatedAtDesc(String entityType, Pageable pageable);
    Page<Activity> findByActivityTypeOrderByCreatedAtDesc(ActivityType activityType, Pageable pageable);
}