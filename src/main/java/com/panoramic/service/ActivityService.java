//package com.panoramic.service;
//
//import com.panoramic.dto.activity.ActivityResponse;
//import com.panoramic.model.Activity;
//import com.panoramic.model.enums.ActivityType;
//import com.panoramic.model.User;
//import com.panoramic.repository.ActivityRepository;
//import com.panoramic.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class ActivityService {
//    private final ActivityRepository activityRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public void recordActivity(User user, ActivityType type, String entityType, Long entityId,
//                               String description, Object metadata) {
//        Activity activity = new Activity();
//        activity.setUser(user);
//        activity.setActivityType(type);
//        activity.setEntityType(entityType);
//        activity.setEntityId(entityId);
//        activity.setDescription(description);
//        activity.setMetadata(serializeMetadata(metadata));
//
//        activityRepository.save(activity);
//    }
//
//    public Page<ActivityResponse> getRecentActivities(Pageable pageable) {
//        return activityRepository.findAllByOrderByCreatedAtDesc(pageable)
//                .map(this::convertToResponse);
//    }
//
//    private ActivityResponse convertToResponse(Activity activity) {
//        ActivityResponse response = new ActivityResponse();
//        response.setId(activity.getId());
//        response.setUser(activity.getUser().getUsername());
//        response.setActivityType(activity.getActivityType());
//        response.setEntityType(activity.getEntityType());
//        response.setEntityId(activity.getEntityId());
//        response.setDescription(activity.getDescription());
//        response.setCreatedAt(activity.getCreatedAt());
//        response.setMetadata(deserializeMetadata(activity.getMetadata()));
//
//        // Set frontend display properties
//        setDisplayProperties(response, activity.getActivityType());
//
//        return response;
//    }
//
//    private void setDisplayProperties(ActivityResponse response, ActivityType type) {
//        switch (type) {
//            case INVENTORY_CREATED, INVENTORY_UPDATED -> {
//                response.setIcon("Package");
//                response.setIconColor("green");
//            }
//            case INVOICE_CREATED, INVOICE_PAID -> {
//                response.setIcon("FileText");
//                response.setIconColor("blue");
//            }
//            case BUDGET_UPDATED -> {
//                response.setIcon("TrendingUp");
//                response.setIconColor("purple");
//            }
//            default -> {
//                response.setIcon("Activity");
//                response.setIconColor("gray");
//            }
//        }
//    }
//
//    // Helper methods for JSON serialization
//    private String serializeMetadata(Object metadata) {
//        // Implement using Jackson or Gson
//    }
//
//    private Object deserializeMetadata(String json) {
//        // Implement using Jackson or Gson
//    }
//}



package com.panoramic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panoramic.dto.activity.ActivityResponse;
import com.panoramic.model.Activity;
import com.panoramic.model.enums.ActivityType;
import com.panoramic.model.User;
import com.panoramic.repository.ActivityRepository;
import com.panoramic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper for JSON serialization

    @Transactional
    public void recordActivity(User user, ActivityType type, String entityType, Long entityId,
                               String description, Object metadata) {
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setActivityType(type);
        activity.setEntityType(entityType);
        activity.setEntityId(entityId);
        activity.setDescription(description);
        activity.setMetadata(serializeMetadata(metadata));

        activityRepository.save(activity);
    }

    public Page<ActivityResponse> getRecentActivities(Pageable pageable) {
        return activityRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToResponse);
    }

    private ActivityResponse convertToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUser(activity.getUser().getUsername());
        response.setActivityType(activity.getActivityType());
        response.setEntityType(activity.getEntityType());
        response.setEntityId(activity.getEntityId());
        response.setDescription(activity.getDescription());
        response.setCreatedAt(activity.getCreatedAt());
        response.setMetadata(deserializeMetadata(activity.getMetadata()));

        // Set frontend display properties
        setDisplayProperties(response, activity.getActivityType());

        return response;
    }

    private void setDisplayProperties(ActivityResponse response, ActivityType type) {
        switch (type) {
            case INVENTORY_CREATED, INVENTORY_UPDATED -> {
                response.setIcon("Package");
                response.setIconColor("green");
            }
            case INVOICE_CREATED, INVOICE_PAID -> {
                response.setIcon("FileText");
                response.setIconColor("blue");
            }
            case BUDGET_UPDATED -> {
                response.setIcon("TrendingUp");
                response.setIconColor("purple");
            }
            default -> {
                response.setIcon("Activity");
                response.setIconColor("gray");
            }
        }
    }

    // Helper method for JSON serialization
    private String serializeMetadata(Object metadata) {
        if (metadata == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize activity metadata", e);
        }
    }

    // Helper method for JSON deserialization
    private Object deserializeMetadata(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize activity metadata", e);
        }
    }
}