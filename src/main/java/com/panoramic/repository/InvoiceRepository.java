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
//
//    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
//
//    Boolean existsByInvoiceNumber(String invoiceNumber);
//
//    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId")
//    Page<Invoice> findByUserId(@Param("userId") Long userId, Pageable pageable);
//
//    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND i.status = :status")
//    Page<Invoice> findByUserIdAndStatus(@Param("userId") Long userId,
//                                        @Param("status") InvoiceStatus status,
//                                        Pageable pageable);
//
//    @Query("SELECT i FROM Invoice i WHERE i.dueDate BETWEEN :startDate AND :endDate AND i.status != 'PAID'")
//    List<Invoice> findOverdueInvoices(@Param("startDate") LocalDate startDate,
//                                      @Param("endDate") LocalDate endDate);
//    Page<Invoice> findByStatus(String status, Pageable pageable);
//
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    Boolean existsByInvoiceNumber(String invoiceNumber);

    // Updated: Get all invoices (removed user filtering)
    @Query("SELECT i FROM Invoice i ORDER BY i.createdAt DESC")
    Page<Invoice> findAllInvoices(Pageable pageable);

    // Updated: Get invoices by status (removed user filtering)
    @Query("SELECT i FROM Invoice i WHERE i.status = :status ORDER BY i.createdAt DESC")
    Page<Invoice> findByStatus(@Param("status") InvoiceStatus status, Pageable pageable);

    // Updated: Find overdue invoices (already global)
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :currentDate AND i.status != 'PAID' ORDER BY i.dueDate ASC")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDate currentDate);

    // Updated: Find invoices by date range (removed user filtering)
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate ORDER BY i.invoiceDate DESC")
    Page<Invoice> findByDateRange(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  Pageable pageable);

    // Updated: Search invoices (removed user filtering)
    @Query("SELECT i FROM Invoice i WHERE " +
            "LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.clientName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.clientEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.businessName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Invoice> findBySearch(@Param("search") String search, Pageable pageable);

    // New: Get invoices by client name
    @Query("SELECT i FROM Invoice i WHERE LOWER(i.clientName) LIKE LOWER(CONCAT('%', :clientName, '%'))")
    Page<Invoice> findByClientName(@Param("clientName") String clientName, Pageable pageable);

    // New: Get invoices by amount range
    @Query("SELECT i FROM Invoice i WHERE i.totalAmount BETWEEN :minAmount AND :maxAmount")
    Page<Invoice> findByAmountRange(@Param("minAmount") BigDecimal minAmount,
                                    @Param("maxAmount") BigDecimal maxAmount,
                                    Pageable pageable);

    // Statistics queries (already global)
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    long countByStatus(@Param("status") InvoiceStatus status);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PAID'")
    BigDecimal sumPaidInvoicesAmount();

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PENDING'")
    BigDecimal sumPendingInvoicesAmount();

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.dueDate < :currentDate AND i.status != 'PAID'")
    BigDecimal sumOverdueInvoicesAmount(@Param("currentDate") LocalDate currentDate);

    // New: Get recent invoices (last 30 days)
    @Query("SELECT i FROM Invoice i WHERE i.createdAt >= :date ORDER BY i.createdAt DESC")
    Page<Invoice> findRecentInvoices(@Param("date") java.time.LocalDateTime date, Pageable pageable);

    // New: Get invoices created by specific user (for audit purposes)
    @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId ORDER BY i.createdAt DESC")
    Page<Invoice> findInvoicesCreatedByUser(@Param("userId") Long userId, Pageable pageable);
}
