package com.panoramic.controller;

import com.panoramic.dto.invoice.InvoiceRequest;
import com.panoramic.model.Invoice;
import com.panoramic.model.enums.InvoiceStatus;
import com.panoramic.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody InvoiceRequest request,
                                               Authentication authentication) {
        Invoice invoice = invoiceService.createInvoice(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }

//    @GetMapping
//    public ResponseEntity<Page<Invoice>> getInvoices(Authentication authentication, Pageable pageable) {
//        Page<Invoice> invoices = invoiceService.getInvoices(authentication, pageable);
//        return ResponseEntity.ok(invoices);
//    }

    @GetMapping
    public ResponseEntity<Page<Invoice>> getInvoices(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(required = false) InvoiceStatus status) {

        Page<Invoice> invoices = invoiceService.getInvoices(authentication, status, pageable);
        return ResponseEntity.ok(invoices);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id, Authentication authentication) {
        Invoice invoice = invoiceService.getInvoiceById(id, authentication);
        return ResponseEntity.ok(invoice);
    }

//    @PatchMapping("/{id}/status")
//    public ResponseEntity<Invoice> updateInvoiceStatus(@PathVariable Long id,
//                                                     @RequestParam InvoiceStatus status,
//                                                     Authentication authentication) {
//        Invoice invoice = invoiceService.updateInvoiceStatus(id, status, authentication);
//        return ResponseEntity.ok(invoice);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id, Authentication authentication) {
        invoiceService.deleteInvoice(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequest request,
            Authentication authentication) {
        Invoice updated = invoiceService.updateInvoice(id, request, authentication);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Invoice> updateInvoiceStatus(
            @PathVariable Long id,
            @RequestParam InvoiceStatus status,
            Authentication authentication) {
        Invoice updated = invoiceService.updateInvoiceStatus(id, status, authentication);
        return ResponseEntity.ok(updated);
    }

}
