package com.panoramic.controller;

import com.panoramic.dto.activity.ActivityResponse;
import com.panoramic.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<Page<ActivityResponse>> getRecentActivities(Pageable pageable) {
        return ResponseEntity.ok(activityService.getRecentActivities(pageable));
    }
}