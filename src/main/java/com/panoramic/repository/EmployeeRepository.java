package com.panoramic.repository;

import com.panoramic.model.Department;
import com.panoramic.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByEmail(String email);
    Boolean existsByEmployeeId(String employeeId);
    Boolean existsByEmail(String email);
    
    List<Employee> findByDepartmentId(Long departmentId);
    List<Employee> findByIsActiveTrue();
    
    @Query("SELECT e FROM Employee e WHERE e.user.id = :userId")
    Page<Employee> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT e FROM Employee e WHERE e.user.id = :userId AND " +
           "(LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.employeeId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Employee> findByUserIdAndSearch(@Param("userId") Long userId, 
                                       @Param("search") String search, 
                                       Pageable pageable);
    Page<Employee> findByDepartment(Department department, Pageable pageable);

    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.isActive = true")
    long countByIsActiveTrue();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.department WHERE e.id = :id")
    Optional<Employee> findByIdWithDepartment(@Param("id") Long id);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.department WHERE e.user.id = :userId")
    Page<Employee> findByUserIdWithDepartment(@Param("userId") Long userId, Pageable pageable);

    List<Employee> findByIsActive(Boolean isActive);
    Page<Employee> findByIsActive(Boolean isActive, Pageable pageable);

}
