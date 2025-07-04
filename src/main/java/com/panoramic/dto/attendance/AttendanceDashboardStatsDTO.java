package com.panoramic.dto.attendance;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDashboardStatsDTO {
    // Today's stats
    private AttendanceStatsDTO todayStats;

    // Trend data for charts
    private List<DailyAttendanceTrendDTO> trendData;

    // Additional dashboard metrics
    private Double averageAttendanceRate;
    private Integer totalWorkingDays;
    private String date;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyAttendanceTrendDTO {
        private String date;
        private Long present;
        private Long absent;
        private Long late;
        private Long onLeave;
        private Double attendanceRate;
    }
}
