package com.panoramic.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {
    private Long id;
    private String companyName;
    private String supplierCode;
    private String contactPerson;
    private String email;
    private String phoneNumber;
    private String website;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String paymentTerms;
    private List<String> supplyCategories;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
