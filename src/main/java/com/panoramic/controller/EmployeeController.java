//package com.panoramic.controller;
//
//import com.panoramic.dto.employee.EmployeeRequest;
//import com.panoramic.dto.employee.EmployeeResponse;
//import com.panoramic.service.EmployeeService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/employees")
//@RequiredArgsConstructor
//public class EmployeeController {
//
//    private final EmployeeService employeeService;
//
//    @PostMapping
//    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request,
//                                                         Authentication authentication) {
//        EmployeeResponse response = employeeService.createEmployee(request, authentication);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<EmployeeResponse>> getEmployees(Authentication authentication, Pageable pageable) {
//        Page<EmployeeResponse> employees = employeeService.getEmployees(authentication, pageable);
//        return ResponseEntity.ok(employees);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id, Authentication authentication) {
//        EmployeeResponse employee = employeeService.getEmployeeById(id, authentication);
//        return ResponseEntity.ok(employee);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
//                                                         @Valid @RequestBody EmployeeRequest request,
//                                                         Authentication authentication) {
//        EmployeeResponse response = employeeService.updateEmployee(id, request, authentication);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, Authentication authentication) {
//        employeeService.deleteEmployee(id, authentication);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(@RequestParam String search,
//                                                                Authentication authentication,
//                                                                Pageable pageable) {
//        Page<EmployeeResponse> employees = employeeService.searchEmployees(search, authentication, pageable);
//        return ResponseEntity.ok(employees);
//    }
//}

//
//package com.panoramic.controller;
//
//import com.panoramic.dto.employee.EmployeeRequest;
//import com.panoramic.dto.employee.EmployeeResponse;
//import com.panoramic.service.EmployeeService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.Resource; // Import Spring's Resource for file streaming
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpHeaders; // For setting Content-Disposition header
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType; // For setting Content-Type header
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile; // Import MultipartFile
//
//@RestController
//@RequestMapping("/api/employees")
//@RequiredArgsConstructor
//public class EmployeeController {
//
//    private final EmployeeService employeeService;
//
//    @PostMapping
//    public ResponseEntity<EmployeeResponse> createEmployee(
//            @Valid @RequestPart("employee") EmployeeRequest request, // Use @RequestPart for the DTO
//            @RequestPart(value = "resume", required = false) MultipartFile resumeFile, // @RequestPart for resume file
//            @RequestPart(value = "idProof", required = false) MultipartFile idProofFile, // @RequestPart for ID proof file
//            Authentication authentication) {
//        EmployeeResponse response = employeeService.createEmployee(request, resumeFile, idProofFile, authentication);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<EmployeeResponse>> getEmployees(Authentication authentication, Pageable pageable) {
//        Page<EmployeeResponse> employees = employeeService.getEmployees(authentication, pageable);
//        return ResponseEntity.ok(employees);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id, Authentication authentication) {
//        EmployeeResponse employee = employeeService.getEmployeeById(id, authentication);
//        return ResponseEntity.ok(employee);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<EmployeeResponse> updateEmployee(
//            @PathVariable Long id,
//            @Valid @RequestPart("employee") EmployeeRequest request, // Use @RequestPart for the DTO
//            @RequestPart(value = "resume", required = false) MultipartFile resumeFile, // @RequestPart for resume file
//            @RequestPart(value = "idProof", required = false) MultipartFile idProofFile, // @RequestPart for ID proof file
//            Authentication authentication) {
//        EmployeeResponse response = employeeService.updateEmployee(id, request, resumeFile, idProofFile, authentication);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, Authentication authentication) {
//        employeeService.deleteEmployee(id, authentication);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(@RequestParam String search,
//                                                                  Authentication authentication,
//                                                                  Pageable pageable) {
//        Page<EmployeeResponse> employees = employeeService.searchEmployees(search, authentication, pageable);
//        return ResponseEntity.ok(employees);
//    }
//
//
//    @GetMapping("/{employeeId}/document/{documentType}")
//    public ResponseEntity<Resource> downloadDocument(
//            @PathVariable Long employeeId,
//            @PathVariable String documentType, // "resume" or "idProof"
//            Authentication authentication) {
//        try {
//            // First, ensure the employee exists and belongs to the current user (security check)
//            // The getEmployeeById method in service already does this. We can reuse it.
//            employeeService.getEmployeeById(employeeId, authentication); // Throws if not found or unauthorized
//
//            // Load file as Resource from service
//            Resource resource = employeeService.loadDocumentAsResource(employeeId, documentType);
//
//            // Determine content type based on the document's stored type
//            String contentType = null;
//            String originalFilename = null;
//
//            // Retrieve the Employee to get the stored content type and filename
//            EmployeeResponse employeeResponse = employeeService.getEmployeeById(employeeId, authentication);
//            if ("resume".equalsIgnoreCase(documentType)) {
//                contentType = employeeResponse.getResumeContentType();
//                originalFilename = employeeResponse.getResumeOriginalFilename();
//            } else if ("idProof".equalsIgnoreCase(documentType)) {
//                contentType = employeeResponse.getIdProofContentType();
//                originalFilename = employeeResponse.getIdProofOriginalFilename();
//            }
//
//            // Fallback content type if not found
//            if (contentType == null || contentType.isBlank()) {
//                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Generic binary stream
//            }
//            if (originalFilename == null || originalFilename.isBlank()) {
//                originalFilename = "downloaded_document"; // Generic filename
//            }
//
//            // Set headers for download
//            HttpHeaders headers = new HttpHeaders();
//            // Force download using attachment disposition
//            headers.setContentDispositionFormData("attachment", originalFilename);
//            // Set content type
//            headers.setContentType(MediaType.parseMediaType(contentType));
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(resource);
//
//        } catch (Exception ex) {
//            // Log the exception for debugging
//            System.err.println("Error downloading document: " + ex.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}

package com.panoramic.controller;

import com.panoramic.dto.employee.EmployeePatchRequest;
import com.panoramic.dto.employee.EmployeeRequest;
import com.panoramic.dto.employee.EmployeeResponse;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.model.Employee;
import com.panoramic.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestPart("employee") EmployeeRequest request,
            @RequestPart(value = "resume", required = false) MultipartFile resumeFile,
            @RequestPart(value = "idProof", required = false) MultipartFile idProofFile,
            Authentication authentication) {
        EmployeeResponse response = employeeService.createEmployee(request, resumeFile, idProofFile, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getEmployees(Authentication authentication, Pageable pageable) {
        Page<EmployeeResponse> employees = employeeService.getEmployees(authentication, pageable);
        return ResponseEntity.ok(employees);
    }
//@GetMapping
//public ResponseEntity<Page<EmployeeResponse>> getEmployees(
//        @RequestParam(required = false) Boolean isActive,
//        Authentication authentication,
//        Pageable pageable) {
//
//    Page<EmployeeResponse> employees = employeeService.getEmployees(authentication, pageable, isActive);
//    return ResponseEntity.ok(employees);
//}


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id, Authentication authentication) {
        EmployeeResponse employee = employeeService.getEmployeeById(id, authentication);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestPart("employee") EmployeeRequest request,
            @RequestPart(value = "resume", required = false) MultipartFile resumeFile,
            @RequestPart(value = "idProof", required = false) MultipartFile idProofFile,
            Authentication authentication) {
        EmployeeResponse response = employeeService.updateEmployee(id, request, resumeFile, idProofFile, authentication);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/basic")
    public ResponseEntity<EmployeeResponse> updateEmployeeBasic(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request,
            Authentication authentication) {
        EmployeeResponse response = employeeService.updateEmployee(id, request, null, null, authentication);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, Authentication authentication) {
        employeeService.deleteEmployee(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(@RequestParam String search,
                                                                  Authentication authentication,
                                                                  Pageable pageable) {
        Page<EmployeeResponse> employees = employeeService.searchEmployees(search, authentication, pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}/document/{documentType}")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long employeeId,
            @PathVariable String documentType,
            Authentication authentication) {
        try {
            Employee employee = employeeService.getEmployeeEntityById(employeeId, authentication);

            String uniqueFilename = null;
            String originalFilename = null;
            String contentType = null;

            if ("resume".equalsIgnoreCase(documentType)) {
                uniqueFilename = employee.getResumePath();
                originalFilename = employee.getResumeFilename();
                contentType = employee.getResumeContentType();
            } else if ("idProof".equalsIgnoreCase(documentType)) {
                uniqueFilename = employee.getIdProofPath();
                originalFilename = employee.getIdProofFilename();
                contentType = employee.getIdProofContentType();
            } else {
                throw new BadRequestException("Invalid document type specified: " + documentType);
            }

            if (uniqueFilename == null || uniqueFilename.isBlank()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = employeeService.loadFileAsResource(uniqueFilename);

            HttpHeaders headers = new HttpHeaders();
            if (contentType == null || contentType.isBlank()) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            headers.setContentType(MediaType.parseMediaType(contentType));

            if (originalFilename == null || originalFilename.isBlank()) {
                originalFilename = "downloaded_document";
            }
            headers.setContentDispositionFormData("attachment", originalFilename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            System.err.println("Error downloading document for employee " + employeeId +
                    ", type " + documentType + ": " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<Page<EmployeeResponse>> getEmployeesByDepartment(@PathVariable Long departmentId,
                                                                           Authentication authentication,
                                                                           Pageable pageable) {
        Page<EmployeeResponse> employees = employeeService.getEmployeesByDepartment(departmentId, authentication, pageable);
        return ResponseEntity.ok(employees);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> patchEmployee(
            @PathVariable Long id,
            @RequestPart("employee") EmployeePatchRequest patchRequest,
            @RequestPart(value = "resume", required = false) MultipartFile resumeFile,
            @RequestPart(value = "idProof", required = false) MultipartFile idProofFile,
            Authentication authentication) {
        EmployeeResponse response = employeeService.patchEmployee(id, patchRequest, resumeFile, idProofFile, authentication);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/basic")
    public ResponseEntity<EmployeeResponse> patchEmployeeBasic(
            @PathVariable Long id,
            @RequestBody EmployeePatchRequest patchRequest,
            Authentication authentication) {
        EmployeeResponse response = employeeService.patchEmployee(id, patchRequest, null, null, authentication);
        return ResponseEntity.ok(response);
    }


}
