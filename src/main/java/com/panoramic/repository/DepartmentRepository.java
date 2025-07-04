package com.panoramic.repository;

import com.panoramic.dto.department.DepartmentDashboardDto;
import com.panoramic.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    Boolean existsByName(String name);
    List<Department> findByIsActiveTrue();
    @Query("SELECT new com.panoramic.dto.department.DepartmentDashboardDto(" +
            "d.id, d.name, " +
            "CAST(COUNT(e.id) as int), " +
            "d.isActive) " +
            "FROM Department d " +
            "LEFT JOIN d.employees e ON e.isActive = true " +
            "WHERE d.isActive = true " +
            "GROUP BY d.id, d.name, d.isActive")
    List<DepartmentDashboardDto> findDepartmentDashboardStats();
}
