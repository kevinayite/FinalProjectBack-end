//package com.panoramic.service;
//
//import com.panoramic.dto.supplier.SupplierRequest;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.exception.ResourceNotFoundException;
//import com.panoramic.model.Supplier;
//import com.panoramic.model.User;
//import com.panoramic.repository.SupplierRepository;
//import com.panoramic.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class SupplierService {
//
//    private final SupplierRepository supplierRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public Supplier createSupplier(SupplierRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        // Check if supplier code already exists
//        if (request.getSupplierCode() != null && supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
//            throw new BadRequestException("Supplier code already exists");
//        }
//
//        // Check if email already exists
//        if (request.getEmail() != null && supplierRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        Supplier supplier = new Supplier();
//        supplier.setCompanyName(request.getCompanyName());
//        supplier.setSupplierCode(request.getSupplierCode());
//        supplier.setContactPerson(request.getContactPerson());
//        supplier.setEmail(request.getEmail());
//        supplier.setPhoneNumber(request.getPhoneNumber());
//        supplier.setWebsite(request.getWebsite());
//        supplier.setAddress(request.getAddress());
//        supplier.setCity(request.getCity());
//        supplier.setState(request.getState());
//        supplier.setPostalCode(request.getPostalCode());
//        supplier.setCountry(request.getCountry());
//        supplier.setPaymentTerms(request.getPaymentTerms());
//        supplier.setSupplyCategories(request.getSupplyCategories());
//        supplier.setNotes(request.getNotes());
//        supplier.setUser(user);
//        supplier.setIsActive(true);
//
//        return supplierRepository.save(supplier);
//    }
//
//    public Page<Supplier> getSuppliers(Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        return supplierRepository.findByUserId(user.getId(), pageable);
//    }
//
//    public Supplier getSupplierById(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Supplier supplier = supplierRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
//
//        // Check if supplier belongs to current user
//        if (!supplier.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Supplier not found");
//        }
//
//        return supplier;
//    }
//
//    @Transactional
//    public Supplier updateSupplier(Long id, SupplierRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Supplier supplier = supplierRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
//
//        // Check if supplier belongs to current user
//        if (!supplier.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Supplier not found");
//        }
//
//        // Check if supplier code already exists (excluding current supplier)
//        if (request.getSupplierCode() != null &&
//            !request.getSupplierCode().equals(supplier.getSupplierCode()) &&
//            supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
//            throw new BadRequestException("Supplier code already exists");
//        }
//
//        // Check if email already exists (excluding current supplier)
//        if (request.getEmail() != null &&
//            !request.getEmail().equals(supplier.getEmail()) &&
//            supplierRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        supplier.setCompanyName(request.getCompanyName());
//        supplier.setSupplierCode(request.getSupplierCode());
//        supplier.setContactPerson(request.getContactPerson());
//        supplier.setEmail(request.getEmail());
//        supplier.setPhoneNumber(request.getPhoneNumber());
//        supplier.setWebsite(request.getWebsite());
//        supplier.setAddress(request.getAddress());
//        supplier.setCity(request.getCity());
//        supplier.setState(request.getState());
//        supplier.setPostalCode(request.getPostalCode());
//        supplier.setCountry(request.getCountry());
//        supplier.setPaymentTerms(request.getPaymentTerms());
//        supplier.setSupplyCategories(request.getSupplyCategories());
//        supplier.setNotes(request.getNotes());
//
//        return supplierRepository.save(supplier);
//    }
//
//    @Transactional
//    public void deleteSupplier(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Supplier supplier = supplierRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
//
//        // Check if supplier belongs to current user
//        if (!supplier.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Supplier not found");
//        }
//
//        supplier.setIsActive(false);
//        supplierRepository.save(supplier);
//    }
//
//    public Page<Supplier> searchSuppliers(String search, Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        return supplierRepository.findByUserIdAndSearch(user.getId(), search, pageable);
//    }
//
//    private User getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//}



//
//package com.panoramic.service;
//
//import com.panoramic.dto.supplier.SupplierRequest;
//import com.panoramic.dto.supplier.SupplierResponse;
//import com.panoramic.dto.supplier.SupplierStatsResponse;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.exception.ResourceNotFoundException;
//import com.panoramic.model.Supplier;
//import com.panoramic.model.User;
//import com.panoramic.repository.SupplierRepository;
//import com.panoramic.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class SupplierService {
//    private final SupplierRepository supplierRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public SupplierResponse createSupplier(SupplierRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        // Check if supplier code already exists
//        if (request.getSupplierCode() != null && supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
//            throw new BadRequestException("Supplier code already exists");
//        }
//
//        // Check if email already exists
//        if (request.getEmail() != null && supplierRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        Supplier supplier = new Supplier();
//        supplier.setCompanyName(request.getCompanyName());
//        supplier.setSupplierCode(request.getSupplierCode());
//        supplier.setContactPerson(request.getContactPerson());
//        supplier.setEmail(request.getEmail());
//        supplier.setPhoneNumber(request.getPhoneNumber());
//        supplier.setWebsite(request.getWebsite());
//        supplier.setAddress(request.getAddress());
//        supplier.setCity(request.getCity());
//        supplier.setState(request.getState());
//        supplier.setPostalCode(request.getPostalCode());
//        supplier.setCountry(request.getCountry());
//        supplier.setPaymentTerms(request.getPaymentTerms());
//        supplier.setSupplyCategories(request.getSupplyCategories());
//        supplier.setNotes(request.getNotes());
//        supplier.setUser(user);
//        supplier.setIsActive(true);
//
//        Supplier savedSupplier = supplierRepository.save(supplier);
//        return convertToResponse(savedSupplier);
//    }
//
//    public Page<SupplierResponse> getSuppliers(Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        Page<Supplier> suppliers = supplierRepository.findByUserId(user.getId(), pageable);
//        return suppliers.map(this::convertToResponse);
//    }
//
//    public SupplierResponse getSupplierById(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Supplier supplier = supplierRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
//
//        // Check if supplier belongs to current user
//        if (!supplier.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Supplier not found");
//        }
//
//        return convertToResponse(supplier);
//    }
//
//    @Transactional
//    public SupplierResponse updateSupplier(Long id, SupplierRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Supplier supplier = supplierRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
//
//        // Check if supplier belongs to current user
//        if (!supplier.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Supplier not found");
//        }
//
//        // Check if supplier code already exists (excluding current supplier)
//        if (request.getSupplierCode() != null &&
//                !request.getSupplierCode().equals(supplier.getSupplierCode()) &&
//                supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
//            throw new BadRequestException("Supplier code already exists");
//        }
//
//        // Check if email already exists (excluding current supplier)
//        if (request.getEmail() != null &&
//                !request.getEmail().equals(supplier.getEmail()) &&
//                supplierRepository.existsByEmail(request.getEmail())) {
//            throw new BadRequestException("Email already exists");
//        }
//
//        supplier.setCompanyName(request.getCompanyName());
//        supplier.setSupplierCode(request.getSupplierCode());
//        supplier.setContactPerson(request.getContactPerson());
//        supplier.setEmail(request.getEmail());
//        supplier.setPhoneNumber(request.getPhoneNumber());
//        supplier.setWebsite(request.getWebsite());
//        supplier.setAddress(request.getAddress());
//        supplier.setCity(request.getCity());
//        supplier.setState(request.getState());
//        supplier.setPostalCode(request.getPostalCode());
//        supplier.setCountry(request.getCountry());
//        supplier.setPaymentTerms(request.getPaymentTerms());
//        supplier.setSupplyCategories(request.getSupplyCategories());
//        supplier.setNotes(request.getNotes());
//
//        Supplier updatedSupplier = supplierRepository.save(supplier);
//        return convertToResponse(updatedSupplier);
//    }
//
//    @Transactional
//    public void deleteSupplier(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Supplier supplier = supplierRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
//
//        // Check if supplier belongs to current user
//        if (!supplier.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Supplier not found");
//        }
//
//        supplier.setIsActive(false);
//        supplierRepository.save(supplier);
//    }
//
//    public Page<SupplierResponse> searchSuppliers(String search, Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        Page<Supplier> suppliers = supplierRepository.findByUserIdAndSearch(user.getId(), search, pageable);
//        return suppliers.map(this::convertToResponse);
//    }
//
//    private User getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//
//    private SupplierResponse convertToResponse(Supplier supplier) {
//        return SupplierResponse.builder()
//                .id(supplier.getId())
//                .companyName(supplier.getCompanyName())
//                .supplierCode(supplier.getSupplierCode())
//                .contactPerson(supplier.getContactPerson())
//                .email(supplier.getEmail())
//                .phoneNumber(supplier.getPhoneNumber())
//                .website(supplier.getWebsite())
//                .address(supplier.getAddress())
//                .city(supplier.getCity())
//                .state(supplier.getState())
//                .postalCode(supplier.getPostalCode())
//                .country(supplier.getCountry())
//                .paymentTerms(supplier.getPaymentTerms())
//                .supplyCategories(supplier.getSupplyCategories())
//                .notes(supplier.getNotes())
//                .isActive(supplier.getIsActive())
//                .createdAt(supplier.getCreatedAt())
//                .updatedAt(supplier.getUpdatedAt())
//                .build();
//    }
//
//    public SupplierStatsResponse getSupplierStats(Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Long userId = user.getId();
//
//        long total = supplierRepository.countByUserId(userId);
//        long active = supplierRepository.countActiveByUserId(userId);
//        long inactive = supplierRepository.countInactiveByUserId(userId);
//        long categories = supplierRepository.countDistinctCategoriesByUserId(userId);
//
//        return new SupplierStatsResponse(total, active, inactive, categories);
//    }
//
//}









package com.panoramic.service;

import com.panoramic.dto.supplier.SupplierRequest;
import com.panoramic.dto.supplier.SupplierResponse;
import com.panoramic.dto.supplier.SupplierStatsResponse;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.model.Supplier;
import com.panoramic.model.User;
import com.panoramic.repository.SupplierRepository;
import com.panoramic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);

        // Check if supplier code already exists
        if (request.getSupplierCode() != null && supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            throw new BadRequestException("Supplier code already exists");
        }

        // Check if email already exists
        if (request.getEmail() != null && supplierRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setCompanyName(request.getCompanyName());
        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhoneNumber(request.getPhoneNumber());
        supplier.setWebsite(request.getWebsite());
        supplier.setAddress(request.getAddress());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setPostalCode(request.getPostalCode());
        supplier.setCountry(request.getCountry());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setSupplyCategories(request.getSupplyCategories());
        supplier.setNotes(request.getNotes());
        supplier.setUser(user); // Still track who created it, but don't filter by it
        supplier.setIsActive(true);

        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToResponse(savedSupplier);
    }

    public Page<SupplierResponse> getSuppliers(Authentication authentication, Pageable pageable) {
        // Remove user filtering - return all suppliers for authenticated users
        Page<Supplier> suppliers = supplierRepository.findAll(pageable);
        return suppliers.map(this::convertToResponse);
    }

    public SupplierResponse getSupplierById(Long id, Authentication authentication) {
        // Remove user ownership check - any authenticated user can access any supplier
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        return convertToResponse(supplier);
    }

    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request, Authentication authentication) {
        // Remove user ownership check - any authenticated user can update any supplier
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        // Check if supplier code already exists (excluding current supplier)
        if (request.getSupplierCode() != null &&
                !request.getSupplierCode().equals(supplier.getSupplierCode()) &&
                supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            throw new BadRequestException("Supplier code already exists");
        }

        // Check if email already exists (excluding current supplier)
        if (request.getEmail() != null &&
                !request.getEmail().equals(supplier.getEmail()) &&
                supplierRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        supplier.setCompanyName(request.getCompanyName());
        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhoneNumber(request.getPhoneNumber());
        supplier.setWebsite(request.getWebsite());
        supplier.setAddress(request.getAddress());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setPostalCode(request.getPostalCode());
        supplier.setCountry(request.getCountry());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setSupplyCategories(request.getSupplyCategories());
        supplier.setNotes(request.getNotes());

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToResponse(updatedSupplier);
    }

    @Transactional
    public void deleteSupplier(Long id, Authentication authentication) {
        // Remove user ownership check - any authenticated user can delete any supplier
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        supplier.setIsActive(false);
        supplierRepository.save(supplier);
    }

    public Page<SupplierResponse> searchSuppliers(String search, Authentication authentication, Pageable pageable) {
        // Remove user filtering from search - search across all suppliers
        Page<Supplier> suppliers = supplierRepository.findBySearch(search, pageable);
        return suppliers.map(this::convertToResponse);
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private SupplierResponse convertToResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .companyName(supplier.getCompanyName())
                .supplierCode(supplier.getSupplierCode())
                .contactPerson(supplier.getContactPerson())
                .email(supplier.getEmail())
                .phoneNumber(supplier.getPhoneNumber())
                .website(supplier.getWebsite())
                .address(supplier.getAddress())
                .city(supplier.getCity())
                .state(supplier.getState())
                .postalCode(supplier.getPostalCode())
                .country(supplier.getCountry())
                .paymentTerms(supplier.getPaymentTerms())
                .supplyCategories(supplier.getSupplyCategories())
                .notes(supplier.getNotes())
                .isActive(supplier.getIsActive())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    public SupplierStatsResponse getSupplierStats(Authentication authentication) {
        // Remove user filtering from stats - show stats for all suppliers
        long total = supplierRepository.count();
        long active = supplierRepository.countActive();
        long inactive = supplierRepository.countInactive();
        long categories = supplierRepository.countDistinctCategories();

        return new SupplierStatsResponse(total, active, inactive, categories);
    }
}
