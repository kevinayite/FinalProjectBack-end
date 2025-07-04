package com.panoramic.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class SupplierRequest {
    @NotBlank(message = "Company name is required")
    private String companyName;

    private String supplierCode;
    private String contactPerson;

    @Email(message = "Invalid email format")
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
}
