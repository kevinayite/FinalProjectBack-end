package com.panoramic.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatsDTO {

    private long present;
    private long absent;
    private long late;
    private long onLeave; // sick_leave + vacation
    private long halfDay;
    private long total;
}
