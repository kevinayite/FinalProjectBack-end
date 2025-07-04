//package com.panoramic.config;
//
//
//
//import com.panoramic.model.Department;
//import com.panoramic.repository.DepartmentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class DepartmentInitializer implements CommandLineRunner {
//
//    private final DepartmentRepository departmentRepository;
//
//    @Override
//    public void run(String... args) {
//        List<Department> departments = List.of(
//                createDepartment(
//                        "HR Department",
//                        "Handles recruitment, onboarding, and employee relations.",
//                        "Alice Johnson"
//                ),
//                createDepartment(
//                        "Finance & Accounting Department",
//                        "Responsible for budgeting, accounting, and financial reporting.",
//                        "Robert Smith"
//                ),
//                createDepartment(
//                        "Inventory Department",
//                        "Manages stock levels, warehouse operations, and inventory tracking.",
//                        "Emily Davis"
//                ),
//                createDepartment(
//                        "Sales Department",
//                        "Drives business growth through client relationships and sales strategies.",
//                        "Michael Brown"
//                ),
//                createDepartment(
//                        "Procurement Department",
//                        "Handles vendor relations, purchasing, and supply chain management.",
//                        "Sophia Wilson"
//                )
//        );
//
//        for (Department department : departments) {
//            Optional<Department> existing = departmentRepository.findByName(department.getName());
//            if (existing.isEmpty()) {
//                departmentRepository.save(department);
//            }
//        }
//    }
//
//    private Department createDepartment(String name, String description, String head) {
//        Department dept = new Department();
//        dept.setName(name);
//        dept.setDescription(description);
//        dept.setDepartmentHead(head);
//        dept.setIsActive(true);
//        dept.setCreatedAt(LocalDateTime.now());
//        dept.setUpdatedAt(LocalDateTime.now());
//        return dept;
//    }
//}
