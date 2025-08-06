//package com.panoramic.controller;
//
//import com.panoramic.dto.invoice.InvoiceRequest;
//import com.panoramic.model.Invoice;
//import com.panoramic.model.enums.InvoiceStatus;
//import com.panoramic.service.InvoiceService;
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
//@RequestMapping("/api/invoices")
//@RequiredArgsConstructor
//public class InvoiceController {
//
//    private final InvoiceService invoiceService;
//
//    @PostMapping
//    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody InvoiceRequest request,
//                                               Authentication authentication) {
//        Invoice invoice = invoiceService.createInvoice(request, authentication);
//        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
//    }
//
////    @GetMapping
////    public ResponseEntity<Page<Invoice>> getInvoices(Authentication authentication, Pageable pageable) {
////        Page<Invoice> invoices = invoiceService.getInvoices(authentication, pageable);
////        return ResponseEntity.ok(invoices);
////    }
//
//    @GetMapping
//    public ResponseEntity<Page<Invoice>> getInvoices(
//            Authentication authentication,
//            Pageable pageable,
//            @RequestParam(required = false) InvoiceStatus status) {
//
//        Page<Invoice> invoices = invoiceService.getInvoices(authentication, status, pageable);
//        return ResponseEntity.ok(invoices);
//    }
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id, Authentication authentication) {
//        Invoice invoice = invoiceService.getInvoiceById(id, authentication);
//        return ResponseEntity.ok(invoice);
//    }
//
////    @PatchMapping("/{id}/status")
////    public ResponseEntity<Invoice> updateInvoiceStatus(@PathVariable Long id,
////                                                     @RequestParam InvoiceStatus status,
////                                                     Authentication authentication) {
////        Invoice invoice = invoiceService.updateInvoiceStatus(id, status, authentication);
////        return ResponseEntity.ok(invoice);
////    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id, Authentication authentication) {
//        invoiceService.deleteInvoice(id, authentication);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<Invoice> updateInvoice(
//            @PathVariable Long id,
//            @Valid @RequestBody InvoiceRequest request,
//            Authentication authentication) {
//        Invoice updated = invoiceService.updateInvoice(id, request, authentication);
//        return ResponseEntity.ok(updated);
//    }
//
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<Invoice> updateInvoiceStatus(
//            @PathVariable Long id,
//            @RequestParam InvoiceStatus status,
//            Authentication authentication) {
//        Invoice updated = invoiceService.updateInvoiceStatus(id, status, authentication);
//        return ResponseEntity.ok(updated);
//    }
//
//}









package com.panoramic.controller;

import com.panoramic.dto.inventory.InventoryItemResponse;
import com.panoramic.dto.invoice.InvoiceRequest;
import com.panoramic.dto.invoice.InvoiceStatsResponse;
import com.panoramic.model.Invoice;
import com.panoramic.model.enums.InvoiceStatus;
import com.panoramic.service.InventoryService;
import com.panoramic.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody InvoiceRequest request,
                                                 Authentication authentication) {
        Invoice invoice = invoiceService.createInvoice(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }

    // Updated: Returns all invoices for any authenticated user
    @GetMapping
    public ResponseEntity<Page<Invoice>> getInvoices(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(required = false) InvoiceStatus status) {
        Page<Invoice> invoices = invoiceService.getInvoices(authentication, status, pageable);
        return ResponseEntity.ok(invoices);
    }

    // Updated: Any authenticated user can access any invoice
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id, Authentication authentication) {
        Invoice invoice = invoiceService.getInvoiceById(id, authentication);
        return ResponseEntity.ok(invoice);
    }

    // Updated: Any authenticated user can delete any invoice
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id, Authentication authentication) {
        invoiceService.deleteInvoice(id, authentication);
        return ResponseEntity.noContent().build();
    }

    // Updated: Any authenticated user can update any invoice
    @PatchMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequest request,
            Authentication authentication) {
        Invoice updated = invoiceService.updateInvoice(id, request, authentication);
        return ResponseEntity.ok(updated);
    }

    // Updated: Any authenticated user can update any invoice status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Invoice> updateInvoiceStatus(
            @PathVariable Long id,
            @RequestParam InvoiceStatus status,
            Authentication authentication) {
        Invoice updated = invoiceService.updateInvoiceStatus(id, status, authentication);
        return ResponseEntity.ok(updated);
    }

    // New: Search invoices across all data
    @GetMapping("/search")
    public ResponseEntity<Page<Invoice>> searchInvoices(
            @RequestParam String search,
            Authentication authentication,
            Pageable pageable) {
        Page<Invoice> invoices = invoiceService.searchInvoices(search, authentication, pageable);
        return ResponseEntity.ok(invoices);
    }

    // New: Get overdue invoices
    @GetMapping("/overdue")
    public ResponseEntity<List<Invoice>> getOverdueInvoices(Authentication authentication) {
        List<Invoice> invoices = invoiceService.getOverdueInvoices(authentication);
        return ResponseEntity.ok(invoices);
    }

    // New: Get invoices by date range
    @GetMapping("/date-range")
    public ResponseEntity<Page<Invoice>> getInvoicesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication,
            Pageable pageable) {
        Page<Invoice> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate, authentication, pageable);
        return ResponseEntity.ok(invoices);
    }

    // New: Get invoices by client name
    @GetMapping("/client/{clientName}")
    public ResponseEntity<Page<Invoice>> getInvoicesByClientName(
            @PathVariable String clientName,
            Authentication authentication,
            Pageable pageable) {
        Page<Invoice> invoices = invoiceService.getInvoicesByClientName(clientName, authentication, pageable);
        return ResponseEntity.ok(invoices);
    }

    // New: Get invoices by amount range
    @GetMapping("/amount-range")
    public ResponseEntity<Page<Invoice>> getInvoicesByAmountRange(
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount,
            Authentication authentication,
            Pageable pageable) {
        Page<Invoice> invoices = invoiceService.getInvoicesByAmountRange(minAmount, maxAmount, authentication, pageable);
        return ResponseEntity.ok(invoices);
    }

    // New: Get invoice statistics
    @GetMapping("/stats")
    public ResponseEntity<InvoiceStatsResponse> getInvoiceStats(Authentication authentication) {
        InvoiceStatsResponse stats = invoiceService.getInvoiceStats(authentication);
        return ResponseEntity.ok(stats);
    }

    // New: Get recent invoices (last 30 days)
    @GetMapping("/recent")
    public ResponseEntity<Page<Invoice>> getRecentInvoices(
            Authentication authentication,
            Pageable pageable) {
        Page<Invoice> invoices = invoiceService.getRecentInvoices(authentication, pageable);
        return ResponseEntity.ok(invoices);
    }

    // New: Get invoices created by specific user (for audit purposes)
    @GetMapping("/created-by/{userId}")
    public ResponseEntity<Page<Invoice>> getInvoicesCreatedByUser(
            @PathVariable Long userId,
            Authentication authentication,
            Pageable pageable) {
        Page<Invoice> invoices = invoiceService.getInvoicesCreatedByUser(userId, authentication, pageable);
        return ResponseEntity.ok(invoices);
    }

    // New: Bulk update invoice status
    @PatchMapping("/bulk-status")
    public ResponseEntity<List<Invoice>> bulkUpdateInvoiceStatus(
            @RequestBody List<Long> invoiceIds,
            @RequestParam InvoiceStatus status,
            Authentication authentication) {
        List<Invoice> updatedInvoices = invoiceService.bulkUpdateInvoiceStatus(invoiceIds, status, authentication);
        return ResponseEntity.ok(updatedInvoices);
    }

    // New: Mark invoice as paid
    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<Invoice> markAsPaid(
            @PathVariable Long id,
            Authentication authentication) {
        Invoice updated = invoiceService.markAsPaid(id, authentication);
        return ResponseEntity.ok(updated);
    }

    // New: Mark invoice as cancelled
    @PatchMapping("/{id}/mark-cancelled")
    public ResponseEntity<Invoice> markAsCancelled(
            @PathVariable Long id,
            Authentication authentication) {
        Invoice updated = invoiceService.markAsCancelled(id, authentication);
        return ResponseEntity.ok(updated);
    }

    // New: Duplicate invoice
    @PostMapping("/{id}/duplicate")
    public ResponseEntity<Invoice> duplicateInvoice(
            @PathVariable Long id,
            @RequestParam String newInvoiceNumber,
            Authentication authentication) {
        Invoice duplicatedInvoice = invoiceService.duplicateInvoice(id, newInvoiceNumber, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(duplicatedInvoice);
    }

    // New: Get invoices by status (alternative endpoint)
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Invoice>> getInvoicesByStatus(
            @PathVariable InvoiceStatus status,
            Authentication authentication,
            Pageable pageable) {
        Page<Invoice> invoices = invoiceService.getInvoices(authentication, status, pageable);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/for-invoice")
    public ResponseEntity<List<InventoryItemResponse>> getInventoryItemsForInvoice(Authentication authentication) {
        List<InventoryItemResponse> items = inventoryService.getAvailableInventoryItems(authentication);
        return ResponseEntity.ok(items);
    }
}
