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

    private AttendanceStatsDTO todayStats;


    private List<DailyAttendanceTrendDTO> trendData;


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
        private long halfDay;
        private Double attendanceRate;
    }
}
