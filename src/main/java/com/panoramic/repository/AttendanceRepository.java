//package com.panoramic.repository;
//
//import com.panoramic.model.Attendance;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
//    Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);
//
//    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(Long employeeId,
//                                                            LocalDate startDate,
//                                                            LocalDate endDate);
//
//    @Query("SELECT a FROM Attendance a WHERE a.employee.user.id = :userId")
//    Page<Attendance> findByUserId(@Param("userId") Long userId, Pageable pageable);
//
//    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate = :date")
//    List<Attendance> findByAttendanceDate(@Param("date") LocalDate date);
//}

package com.panoramic.repository;

import com.panoramic.model.Attendance;
import com.panoramic.model.Employee;
import com.panoramic.model.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find attendance by employee and date
    Optional<Attendance> findByEmployeeAndAttendanceDate(Employee employee, LocalDate attendanceDate);

    // Find attendance by employee ID and date
    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId AND a.attendanceDate = :date")
    Optional<Attendance> findByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    // Find all attendance records for an employee
    List<Attendance> findByEmployeeOrderByAttendanceDateDesc(Employee employee);

    // Find attendance records for an employee within date range
    @Query("SELECT a FROM Attendance a WHERE a.employee = :employee AND a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC")
    List<Attendance> findByEmployeeAndDateRange(@Param("employee") Employee employee,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    // Find all attendance records for a specific date
    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

    // Count attendance by status and date
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.attendanceDate = :date AND a.status = :status")
    Long countByDateAndStatus(@Param("date") LocalDate date, @Param("status") AttendanceStatus status);

    // Find attendance records by date range
    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC, a.employee.firstName ASC")
    List<Attendance> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Get monthly attendance summary for an employee
    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId AND YEAR(a.attendanceDate) = :year AND MONTH(a.attendanceDate) = :month ORDER BY a.attendanceDate DESC")
    List<Attendance> findMonthlyAttendance(@Param("employeeId") Long employeeId, @Param("year") int year, @Param("month") int month);

    // Check if employee has attendance record for date
    boolean existsByEmployeeAndAttendanceDate(Employee employee, LocalDate attendanceDate);

    // Get attendance statistics for a date
    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.attendanceDate = :date GROUP BY a.status")
    List<Object[]> getAttendanceStatsByDate(@Param("date") LocalDate date);

    // Find recent attendance records for an employee (last N records)
    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId ORDER BY a.attendanceDate DESC LIMIT :limit")
    List<Attendance> findRecentAttendanceByEmployee(@Param("employeeId") Long employeeId, @Param("limit") int limit);

    List<Attendance> findByEmployeeAndAttendanceDateBetween(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate
    );
}
