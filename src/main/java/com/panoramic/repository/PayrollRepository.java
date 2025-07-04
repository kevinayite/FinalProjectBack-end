package com.panoramic.repository;

import com.panoramic.model.Employee;
import com.panoramic.model.PayrollRecord;
import com.panoramic.model.enums.PayrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollRecord, Long> {

    List<PayrollRecord> findByEmployeeAndPayPeriodStartBetween(Employee employee, LocalDate start, LocalDate end);

    List<PayrollRecord> findByPayPeriodStartBetween(LocalDate start, LocalDate end);

    List<PayrollRecord> findByStatus(PayrollStatus status);

    Page<PayrollRecord> findByEmployee(Employee employee, Pageable pageable);

    Optional<PayrollRecord> findByEmployeeAndPayPeriodStartAndPayPeriodEnd(
            Employee employee, LocalDate payPeriodStart, LocalDate payPeriodEnd);

    @Query("SELECT pr FROM PayrollRecord pr WHERE pr.employee.id = :employeeId AND " +
            "pr.payPeriodStart >= :startDate AND pr.payPeriodEnd <= :endDate")
    List<PayrollRecord> findPayrollRecordsByEmployeeAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
