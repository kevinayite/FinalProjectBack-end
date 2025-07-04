package com.panoramic.service;


import com.panoramic.dto.payroll.PayrollConfigurationDto;
import com.panoramic.model.PayrollConfiguration;
import com.panoramic.repository.PayrollConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollConfigurationService {

    private final PayrollConfigurationRepository payrollConfigurationRepository;

    public PayrollConfigurationDto getActiveConfiguration() {
        PayrollConfiguration config = payrollConfigurationRepository
                .findByIsActive(true)
                .orElseGet(this::createDefaultConfiguration);
        return convertToDto(config);
    }

    public PayrollConfiguration getActiveConfigurationEntity() {
        return payrollConfigurationRepository
                .findByIsActive(true)
                .orElseGet(this::createDefaultConfiguration);
    }

    public List<PayrollConfigurationDto> getAllConfigurations() {
        return payrollConfigurationRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PayrollConfigurationDto getConfigurationById(Long id) {
        PayrollConfiguration config = payrollConfigurationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll configuration not found with id: " + id));
        return convertToDto(config);
    }

    public PayrollConfigurationDto createConfiguration(PayrollConfigurationDto dto) {
        PayrollConfiguration config = convertToEntity(dto);
        config = payrollConfigurationRepository.save(config);
        return convertToDto(config);
    }

    public PayrollConfigurationDto updateConfiguration(Long id, PayrollConfigurationDto dto) {
        PayrollConfiguration existingConfig = payrollConfigurationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll configuration not found with id: " + id));

        updateEntityFromDto(existingConfig, dto);
        existingConfig = payrollConfigurationRepository.save(existingConfig);
        return convertToDto(existingConfig);
    }

    public void deleteConfiguration(Long id) {
        if (!payrollConfigurationRepository.existsById(id)) {
            throw new RuntimeException("Payroll configuration not found with id: " + id);
        }
        payrollConfigurationRepository.deleteById(id);
    }

    public PayrollConfigurationDto activateConfiguration(Long id) {
        // Deactivate all configurations
        List<PayrollConfiguration> allConfigs = payrollConfigurationRepository.findAll();
        allConfigs.forEach(config -> config.setIsActive(false));
        payrollConfigurationRepository.saveAll(allConfigs);

        // Activate the selected configuration
        PayrollConfiguration configToActivate = payrollConfigurationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll configuration not found with id: " + id));

        configToActivate.setIsActive(true);
        configToActivate = payrollConfigurationRepository.save(configToActivate);
        return convertToDto(configToActivate);
    }

    private PayrollConfiguration createDefaultConfiguration() {
        PayrollConfiguration defaultConfig = new PayrollConfiguration();
        defaultConfig.setConfigName("DEFAULT");
        defaultConfig.setIsActive(true);
        return payrollConfigurationRepository.save(defaultConfig);
    }

    private PayrollConfigurationDto convertToDto(PayrollConfiguration entity) {
        PayrollConfigurationDto dto = new PayrollConfigurationDto();
        dto.setId(entity.getId());
        dto.setConfigName(entity.getConfigName());
        dto.setDefaultProvidentFundRate(entity.getDefaultProvidentFundRate());
        dto.setDefaultHealthInsurance(entity.getDefaultHealthInsurance());
        dto.setOvertimeRateMultiplier(entity.getOvertimeRateMultiplier());
        dto.setLatePenaltyPerOccurrence(entity.getLatePenaltyPerOccurrence());
        dto.setAbsentPenaltyPerDay(entity.getAbsentPenaltyPerDay());
        dto.setStandardWorkHoursPerDay(entity.getStandardWorkHoursPerDay());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private PayrollConfiguration convertToEntity(PayrollConfigurationDto dto) {
        PayrollConfiguration entity = new PayrollConfiguration();
        entity.setConfigName(dto.getConfigName());
        entity.setDefaultProvidentFundRate(dto.getDefaultProvidentFundRate());
        entity.setDefaultHealthInsurance(dto.getDefaultHealthInsurance());
        entity.setOvertimeRateMultiplier(dto.getOvertimeRateMultiplier());
        entity.setLatePenaltyPerOccurrence(dto.getLatePenaltyPerOccurrence());
        entity.setAbsentPenaltyPerDay(dto.getAbsentPenaltyPerDay());
        entity.setStandardWorkHoursPerDay(dto.getStandardWorkHoursPerDay());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : false);
        return entity;
    }

    private void updateEntityFromDto(PayrollConfiguration entity, PayrollConfigurationDto dto) {
        entity.setConfigName(dto.getConfigName());
        entity.setDefaultProvidentFundRate(dto.getDefaultProvidentFundRate());
        entity.setDefaultHealthInsurance(dto.getDefaultHealthInsurance());
        entity.setOvertimeRateMultiplier(dto.getOvertimeRateMultiplier());
        entity.setLatePenaltyPerOccurrence(dto.getLatePenaltyPerOccurrence());
        entity.setAbsentPenaltyPerDay(dto.getAbsentPenaltyPerDay());
        entity.setStandardWorkHoursPerDay(dto.getStandardWorkHoursPerDay());
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
    }
}
