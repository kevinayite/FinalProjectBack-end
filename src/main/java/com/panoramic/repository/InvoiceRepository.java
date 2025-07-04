//package com.panoramic.repository;
//
//import com.panoramic.model.Invoice;
//import com.panoramic.model.enums.InvoiceStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
//    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
//    Boolean existsByInvoiceNumber(String invoiceNumber);
//
//    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId")
//    Page<Invoice> findByUserId(@Param("userId") Long userId, Pageable pageable);
//
//    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND i.status = :status")
//    Page<Invoice> findByUserIdAndStatus(@Param("userId") Long userId,
//                                      @Param("status") InvoiceStatus status,
//                                      Pageable pageable);
//
//    @Query("SELECT i FROM Invoice i WHERE i.dueDateBetween(:startDate, :endDate) AND i.status != 'PAID'")
//    List<Invoice> findOverdueInvoices(@Param("startDate") LocalDate startDate,
//                                    @Param("endDate") LocalDate endDate);
//}


package com.panoramic.repository;

import com.panoramic.model.Invoice;
import com.panoramic.model.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId")
    Page<Invoice> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND i.status = :status")
    Page<Invoice> findByUserIdAndStatus(@Param("userId") Long userId,
                                        @Param("status") InvoiceStatus status,
                                        Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.dueDate BETWEEN :startDate AND :endDate AND i.status != 'PAID'")
    List<Invoice> findOverdueInvoices(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
    Page<Invoice> findByStatus(String status, Pageable pageable);

}
