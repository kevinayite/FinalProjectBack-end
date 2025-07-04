package com.panoramic.controller;

import com.panoramic.dto.payroll.PayrollConfigurationDto;
import com.panoramic.service.PayrollConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payroll-configuration")
@RequiredArgsConstructor

public class PayrollConfigurationController {

    private final PayrollConfigurationService payrollConfigurationService;

    @GetMapping
    public ResponseEntity<List<PayrollConfigurationDto>> getAllConfigurations() {
        List<PayrollConfigurationDto> configurations = payrollConfigurationService.getAllConfigurations();
        return ResponseEntity.ok(configurations);
    }

    @GetMapping("/active")
    public ResponseEntity<PayrollConfigurationDto> getActiveConfiguration() {
        PayrollConfigurationDto configuration = payrollConfigurationService.getActiveConfiguration();
        return ResponseEntity.ok(configuration);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollConfigurationDto> getConfigurationById(@PathVariable Long id) {
        PayrollConfigurationDto configuration = payrollConfigurationService.getConfigurationById(id);
        return ResponseEntity.ok(configuration);
    }

    @PostMapping
    public ResponseEntity<PayrollConfigurationDto> createConfiguration(@Valid @RequestBody PayrollConfigurationDto dto) {
        PayrollConfigurationDto createdConfiguration = payrollConfigurationService.createConfiguration(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConfiguration);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollConfigurationDto> updateConfiguration(
            @PathVariable Long id,
            @Valid @RequestBody PayrollConfigurationDto dto) {
        PayrollConfigurationDto updatedConfiguration = payrollConfigurationService.updateConfiguration(id, dto);
        return ResponseEntity.ok(updatedConfiguration);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<PayrollConfigurationDto> activateConfiguration(@PathVariable Long id) {
        PayrollConfigurationDto activatedConfiguration = payrollConfigurationService.activateConfiguration(id);
        return ResponseEntity.ok(activatedConfiguration);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable Long id) {
        payrollConfigurationService.deleteConfiguration(id);
        return ResponseEntity.noContent().build();
    }
}
