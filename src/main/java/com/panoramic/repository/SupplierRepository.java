//package com.panoramic.repository;
//
//import com.panoramic.model.Supplier;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface SupplierRepository extends JpaRepository<Supplier, Long> {
//    Optional<Supplier> findBySupplierCode(String supplierCode);
//    Optional<Supplier> findByEmail(String email);
//    Boolean existsBySupplierCode(String supplierCode);
//    Boolean existsByEmail(String email);
//
//    @Query("SELECT s FROM Supplier s WHERE s.user.id = :userId")
//    Page<Supplier> findByUserId(@Param("userId") Long userId, Pageable pageable);
//
//    @Query("SELECT s FROM Supplier s WHERE s.user.id = :userId AND " +
//           "(LOWER(s.companyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//           "LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//           "LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :search, '%')))")
//    Page<Supplier> findByUserIdAndSearch(@Param("userId") Long userId,
//                                       @Param("search") String search,
//                                       Pageable pageable);
//
//    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.user.id = :userId")
//    long countByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.user.id = :userId AND s.isActive = true")
//    long countActiveByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.user.id = :userId AND s.isActive = false")
//    long countInactiveByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT COUNT(DISTINCT c) FROM Supplier s JOIN s.supplyCategories c WHERE s.user.id = :userId")
//    long countDistinctCategoriesByUserId(@Param("userId") Long userId);
//
//}






package com.panoramic.repository;

import com.panoramic.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findBySupplierCode(String supplierCode);
    Optional<Supplier> findByEmail(String email);
    Boolean existsBySupplierCode(String supplierCode);
    Boolean existsByEmail(String email);

    // Updated queries without user filtering
    @Query("SELECT s FROM Supplier s WHERE " +
            "(LOWER(s.companyName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.supplierCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Supplier> findBySearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = true")
    long countActive();

    @Query("SELECT COUNT(s) FROM Supplier s WHERE s.isActive = false")
    long countInactive();

    @Query("SELECT COUNT(DISTINCT c) FROM Supplier s JOIN s.supplyCategories c")
    long countDistinctCategories();
}
