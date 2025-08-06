package com.panoramic.dto.activity;


import com.panoramic.model.enums.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityResponse {
    private Long id;
    private String user; // username or display name
    private ActivityType activityType;
    private String entityType;
    private Long entityId;
    private String description;
    private LocalDateTime createdAt;
    private Object metadata; // deserialized JSON

    // Frontend display fields
    private String icon;
    private String iconColor;
}