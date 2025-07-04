package com.panoramic.service;

import com.panoramic.dto.invoice.InvoiceRequest;
import com.panoramic.dto.invoice.InvoiceItemRequest;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.model.Invoice;
import com.panoramic.model.InvoiceItem;
import com.panoramic.model.User;
import com.panoramic.model.enums.InvoiceStatus;
import com.panoramic.repository.InvoiceRepository;
import com.panoramic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Transactional
    public Invoice createInvoice(InvoiceRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);

        // Check if invoice number already exists
        if (invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
            throw new BadRequestException("Invoice number already exists");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setClientName(request.getClientName());
        invoice.setClientEmail(request.getClientEmail());
        invoice.setClientAddress(request.getClientAddress());
        invoice.setBusinessName(request.getBusinessName());
        invoice.setBusinessEmail(request.getBusinessEmail());
        invoice.setBusinessAddress(request.getBusinessAddress());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setTaxRate(request.getTaxRate() != null ? request.getTaxRate() : BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setUser(user);

        // Calculate totals
        BigDecimal subtotal = BigDecimal.ZERO;
        List<InvoiceItem> items = new ArrayList<>();

        for (InvoiceItemRequest itemRequest : request.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemRequest.getDescription());
            item.setQuantity(itemRequest.getQuantity());
            item.setRate(itemRequest.getRate());
            
            BigDecimal amount = itemRequest.getRate().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            item.setAmount(amount);
            item.setInvoice(invoice);
            
            items.add(item);
            subtotal = subtotal.add(amount);
        }

        invoice.setSubtotal(subtotal);
        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(subtotal.add(taxAmount));
        invoice.setItems(items);

        return invoiceRepository.save(invoice);
    }

    public Page<Invoice> getInvoices(Authentication authentication, Pageable pageable) {
        User user = getCurrentUser(authentication);
        return invoiceRepository.findByUserId(user.getId(), pageable);
    }

    public Invoice getInvoiceById(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        
        // Check if invoice belongs to current user
        if (!invoice.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        
        return invoice;
    }

    @Transactional
    public Invoice updateInvoiceStatus(Long id, InvoiceStatus status, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        
        // Check if invoice belongs to current user
        if (!invoice.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Page<Invoice> getInvoices(Authentication authentication, InvoiceStatus status, Pageable pageable) {
        User user = getCurrentUser(authentication);

        if (status != null) {
            return invoiceRepository.findByUserIdAndStatus(user.getId(), status, pageable);
        }

        return invoiceRepository.findByUserId(user.getId(), pageable);
    }

    @Transactional
    public void deleteInvoice(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (!invoice.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Invoice not found");
        }

        invoiceRepository.delete(invoice);
    }

//
//    @Transactional
//    public Invoice updateInvoice(Long id, InvoiceRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        if (!invoice.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Invoice not found");
//        }
//
//        // Update invoice details
//        invoice.setInvoiceNumber(request.getInvoiceNumber());
//        invoice.setClientName(request.getClientName());
//        invoice.setClientEmail(request.getClientEmail());
//        invoice.setClientAddress(request.getClientAddress());
//        invoice.setBusinessName(request.getBusinessName());
//        invoice.setBusinessEmail(request.getBusinessEmail());
//        invoice.setBusinessAddress(request.getBusinessAddress());
//        invoice.setInvoiceDate(request.getInvoiceDate());
//        invoice.setDueDate(request.getDueDate());
//        invoice.setTaxRate(request.getTaxRate() != null ? request.getTaxRate() : BigDecimal.ZERO);
//
//        // Remove existing items if updating all items
//        invoice.getItems().clear();
//
//        BigDecimal subtotal = BigDecimal.ZERO;
//        List<InvoiceItem> updatedItems = new ArrayList<>();
//
//        for (InvoiceItemRequest itemRequest : request.getItems()) {
//            InvoiceItem item = new InvoiceItem();
//            item.setDescription(itemRequest.getDescription());
//            item.setQuantity(itemRequest.getQuantity());
//            item.setRate(itemRequest.getRate());
//
//            BigDecimal amount = itemRequest.getRate().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
//            item.setAmount(amount);
//            item.setInvoice(invoice);
//
//            updatedItems.add(item);
//            subtotal = subtotal.add(amount);
//        }
//
//        invoice.setItems(updatedItems);
//        invoice.setSubtotal(subtotal);
//
//        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
//        invoice.setTaxAmount(taxAmount);
//        invoice.setTotalAmount(subtotal.add(taxAmount));
//
//        return invoiceRepository.save(invoice);
//    }
//
//

    @Transactional
    public Invoice updateInvoice(Long id, InvoiceRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (!invoice.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Invoice not found");
        }

        // Update invoice details
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setClientName(request.getClientName());
        invoice.setClientEmail(request.getClientEmail());
        invoice.setClientAddress(request.getClientAddress());
        invoice.setBusinessName(request.getBusinessName());
        invoice.setBusinessEmail(request.getBusinessEmail());
        invoice.setBusinessAddress(request.getBusinessAddress());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setTaxRate(request.getTaxRate() != null ? request.getTaxRate() : BigDecimal.ZERO);

        // ✅ Set status if provided
        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }

        // Remove existing items if updating all items
        invoice.getItems().clear();

        BigDecimal subtotal = BigDecimal.ZERO;
        List<InvoiceItem> updatedItems = new ArrayList<>();

        for (InvoiceItemRequest itemRequest : request.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemRequest.getDescription());
            item.setQuantity(itemRequest.getQuantity());
            item.setRate(itemRequest.getRate());

            BigDecimal amount = itemRequest.getRate().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            item.setAmount(amount);
            item.setInvoice(invoice);

            updatedItems.add(item);
            subtotal = subtotal.add(amount);
        }

        invoice.setItems(updatedItems);
        invoice.setSubtotal(subtotal);

        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(subtotal.add(taxAmount));

        return invoiceRepository.save(invoice);
    }


}
