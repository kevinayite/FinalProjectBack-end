package com.panoramic.controller;


import com.panoramic.dto.department.DepartmentDashboardDto;
import com.panoramic.dto.department.DepartmentRequestDto;
import com.panoramic.dto.department.DepartmentResponseDto;
import com.panoramic.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(@RequestBody DepartmentRequestDto requestDto) {
        return ResponseEntity.ok(departmentService.createDepartment(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequestDto requestDto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/dashboard-stats")
//    public ResponseEntity<List<DepartmentDashboardDto>> getDepartmentDashboardStats() {
//        return ResponseEntity.ok(departmentService.getDepartmentDashboardStats());
//    }
@GetMapping("/dashboard-stats")
public ResponseEntity<List<DepartmentDashboardDto>> getDepartmentDashboardStats() {
    try {
        List<DepartmentDashboardDto> stats = departmentService.getDepartmentDashboardStats();
        log.info("Successfully retrieved dashboard stats for {} departments", stats.size());
        return ResponseEntity.ok(stats);
    } catch (Exception e) {
        log.error("Error retrieving department dashboard stats", e);
        return ResponseEntity.internalServerError().build();
    }
}

}
