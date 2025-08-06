//package com.panoramic.controller;
//
//import com.panoramic.dto.supplier.SupplierRequest;
//import com.panoramic.model.Supplier;
//import com.panoramic.service.SupplierService;
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
//@RequestMapping("/api/suppliers")
//@RequiredArgsConstructor
//public class SupplierController {
//
//    private final SupplierService supplierService;
//
//    @PostMapping
//    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody SupplierRequest request,
//                                                  Authentication authentication) {
//        Supplier supplier = supplierService.createSupplier(request, authentication);
//        return ResponseEntity.status(HttpStatus.CREATED).body(supplier);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<Supplier>> getSuppliers(Authentication authentication, Pageable pageable) {
//        Page<Supplier> suppliers = supplierService.getSuppliers(authentication, pageable);
//        return ResponseEntity.ok(suppliers);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id, Authentication authentication) {
//        Supplier supplier = supplierService.getSupplierById(id, authentication);
//        return ResponseEntity.ok(supplier);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id,
//                                                  @Valid @RequestBody SupplierRequest request,
//                                                  Authentication authentication) {
//        Supplier supplier = supplierService.updateSupplier(id, request, authentication);
//        return ResponseEntity.ok(supplier);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id, Authentication authentication) {
//        supplierService.deleteSupplier(id, authentication);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Page<Supplier>> searchSuppliers(@RequestParam String search,
//                                                        Authentication authentication,
//                                                        Pageable pageable) {
//        Page<Supplier> suppliers = supplierService.searchSuppliers(search, authentication, pageable);
//        return ResponseEntity.ok(suppliers);
//    }
//}







//package com.panoramic.controller;
//
//import com.panoramic.dto.supplier.SupplierRequest;
//import com.panoramic.dto.supplier.SupplierResponse;
//import com.panoramic.dto.supplier.SupplierStatsResponse;
//import com.panoramic.service.SupplierService;
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
//@RequestMapping("/api/suppliers")
//@RequiredArgsConstructor
//public class SupplierController {
//    private final SupplierService supplierService;
//
//    @PostMapping
//    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody SupplierRequest request,
//                                                           Authentication authentication) {
//        SupplierResponse supplier = supplierService.createSupplier(request, authentication);
//        return ResponseEntity.status(HttpStatus.CREATED).body(supplier);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<SupplierResponse>> getSuppliers(Authentication authentication, Pageable pageable) {
//        Page<SupplierResponse> suppliers = supplierService.getSuppliers(authentication, pageable);
//        return ResponseEntity.ok(suppliers);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id, Authentication authentication) {
//        SupplierResponse supplier = supplierService.getSupplierById(id, authentication);
//        return ResponseEntity.ok(supplier);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable Long id,
//                                                           @Valid @RequestBody SupplierRequest request,
//                                                           Authentication authentication) {
//        SupplierResponse supplier = supplierService.updateSupplier(id, request, authentication);
//        return ResponseEntity.ok(supplier);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id, Authentication authentication) {
//        supplierService.deleteSupplier(id, authentication);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Page<SupplierResponse>> searchSuppliers(@RequestParam String search,
//                                                                  Authentication authentication,
//                                                                  Pageable pageable) {
//        Page<SupplierResponse> suppliers = supplierService.searchSuppliers(search, authentication, pageable);
//        return ResponseEntity.ok(suppliers);
//    }
//
//    @GetMapping("/stats")
//    public ResponseEntity<SupplierStatsResponse> getSupplierStats(Authentication authentication) {
//        SupplierStatsResponse stats = supplierService.getSupplierStats(authentication);
//        return ResponseEntity.ok(stats);
//    }
//
//}











package com.panoramic.controller;

import com.panoramic.dto.supplier.SupplierRequest;
import com.panoramic.dto.supplier.SupplierResponse;
import com.panoramic.dto.supplier.SupplierStatsResponse;
import com.panoramic.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody SupplierRequest request,
                                                           Authentication authentication) {
        SupplierResponse supplier = supplierService.createSupplier(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(supplier);
    }

    @GetMapping
    public ResponseEntity<Page<SupplierResponse>> getSuppliers(Authentication authentication, Pageable pageable) {
        Page<SupplierResponse> suppliers = supplierService.getSuppliers(authentication, pageable);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable Long id, Authentication authentication) {
        SupplierResponse supplier = supplierService.getSupplierById(id, authentication);
        return ResponseEntity.ok(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable Long id,
                                                           @Valid @RequestBody SupplierRequest request,
                                                           Authentication authentication) {
        SupplierResponse supplier = supplierService.updateSupplier(id, request, authentication);
        return ResponseEntity.ok(supplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id, Authentication authentication) {
        supplierService.deleteSupplier(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SupplierResponse>> searchSuppliers(@RequestParam String search,
                                                                  Authentication authentication,
                                                                  Pageable pageable) {
        Page<SupplierResponse> suppliers = supplierService.searchSuppliers(search, authentication, pageable);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/stats")
    public ResponseEntity<SupplierStatsResponse> getSupplierStats(Authentication authentication) {
        SupplierStatsResponse stats = supplierService.getSupplierStats(authentication);
        return ResponseEntity.ok(stats);
    }
}
