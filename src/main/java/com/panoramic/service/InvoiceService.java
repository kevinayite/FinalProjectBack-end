//package com.panoramic.service;
//
//import com.panoramic.dto.invoice.InvoiceRequest;
//import com.panoramic.dto.invoice.InvoiceItemRequest;
//import com.panoramic.exception.BadRequestException;
//import com.panoramic.exception.ResourceNotFoundException;
//import com.panoramic.model.Invoice;
//import com.panoramic.model.InvoiceItem;
//import com.panoramic.model.User;
//import com.panoramic.model.enums.InvoiceStatus;
//import com.panoramic.repository.InvoiceRepository;
//import com.panoramic.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class InvoiceService {
//
//    private final InvoiceRepository invoiceRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public Invoice createInvoice(InvoiceRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        // Check if invoice number already exists
//        if (invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
//            throw new BadRequestException("Invoice number already exists");
//        }
//
//        Invoice invoice = new Invoice();
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
//        invoice.setStatus(InvoiceStatus.PENDING);
//        invoice.setUser(user);
//
//        // Calculate totals
//        BigDecimal subtotal = BigDecimal.ZERO;
//        List<InvoiceItem> items = new ArrayList<>();
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
//            items.add(item);
//            subtotal = subtotal.add(amount);
//        }
//
//        invoice.setSubtotal(subtotal);
//        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
//        invoice.setTaxAmount(taxAmount);
//        invoice.setTotalAmount(subtotal.add(taxAmount));
//        invoice.setItems(items);
//
//        return invoiceRepository.save(invoice);
//    }
//
//    public Page<Invoice> getInvoices(Authentication authentication, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//        return invoiceRepository.findByUserId(user.getId(), pageable);
//    }
//
//    public Invoice getInvoiceById(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        // Check if invoice belongs to current user
//        if (!invoice.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Invoice not found");
//        }
//
//        return invoice;
//    }
//
//    @Transactional
//    public Invoice updateInvoiceStatus(Long id, InvoiceStatus status, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        // Check if invoice belongs to current user
//        if (!invoice.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Invoice not found");
//        }
//
//        invoice.setStatus(status);
//        return invoiceRepository.save(invoice);
//    }
//
//    private User getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//    }
//
//    public Page<Invoice> getInvoices(Authentication authentication, InvoiceStatus status, Pageable pageable) {
//        User user = getCurrentUser(authentication);
//
//        if (status != null) {
//            return invoiceRepository.findByUserIdAndStatus(user.getId(), status, pageable);
//        }
//
//        return invoiceRepository.findByUserId(user.getId(), pageable);
//    }
//
//    @Transactional
//    public void deleteInvoice(Long id, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        if (!invoice.getUser().getId().equals(user.getId())) {
//            throw new ResourceNotFoundException("Invoice not found");
//        }
//
//        invoiceRepository.delete(invoice);
//    }
//
////
////    @Transactional
////    public Invoice updateInvoice(Long id, InvoiceRequest request, Authentication authentication) {
////        User user = getCurrentUser(authentication);
////
////        Invoice invoice = invoiceRepository.findById(id)
////                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
////
////        if (!invoice.getUser().getId().equals(user.getId())) {
////            throw new ResourceNotFoundException("Invoice not found");
////        }
////
////        // Update invoice details
////        invoice.setInvoiceNumber(request.getInvoiceNumber());
////        invoice.setClientName(request.getClientName());
////        invoice.setClientEmail(request.getClientEmail());
////        invoice.setClientAddress(request.getClientAddress());
////        invoice.setBusinessName(request.getBusinessName());
////        invoice.setBusinessEmail(request.getBusinessEmail());
////        invoice.setBusinessAddress(request.getBusinessAddress());
////        invoice.setInvoiceDate(request.getInvoiceDate());
////        invoice.setDueDate(request.getDueDate());
////        invoice.setTaxRate(request.getTaxRate() != null ? request.getTaxRate() : BigDecimal.ZERO);
////
////        // Remove existing items if updating all items
////        invoice.getItems().clear();
////
////        BigDecimal subtotal = BigDecimal.ZERO;
////        List<InvoiceItem> updatedItems = new ArrayList<>();
////
////        for (InvoiceItemRequest itemRequest : request.getItems()) {
////            InvoiceItem item = new InvoiceItem();
////            item.setDescription(itemRequest.getDescription());
////            item.setQuantity(itemRequest.getQuantity());
////            item.setRate(itemRequest.getRate());
////
////            BigDecimal amount = itemRequest.getRate().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
////            item.setAmount(amount);
////            item.setInvoice(invoice);
////
////            updatedItems.add(item);
////            subtotal = subtotal.add(amount);
////        }
////
////        invoice.setItems(updatedItems);
////        invoice.setSubtotal(subtotal);
////
////        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
////        invoice.setTaxAmount(taxAmount);
////        invoice.setTotalAmount(subtotal.add(taxAmount));
////
////        return invoiceRepository.save(invoice);
////    }
////
////
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
//        // ✅ Set status if provided
//        if (request.getStatus() != null) {
//            invoice.setStatus(request.getStatus());
//        }
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
//}










package com.panoramic.service;

import com.panoramic.dto.invoice.InvoiceRequest;
import com.panoramic.dto.invoice.InvoiceItemRequest;
import com.panoramic.dto.invoice.InvoiceStatsResponse;
import com.panoramic.exception.BadRequestException;
import com.panoramic.exception.ResourceNotFoundException;
import com.panoramic.model.InventoryItem;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

//    @Transactional
//    public Invoice createInvoice(InvoiceRequest request, Authentication authentication) {
//        User user = getCurrentUser(authentication);
//
//        // Check if invoice number already exists
//        if (invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
//            throw new BadRequestException("Invoice number already exists");
//        }
//
//        Invoice invoice = new Invoice();
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
//        invoice.setStatus(InvoiceStatus.PENDING);
//        invoice.setUser(user); // Still track who created it, but don't filter by it
//
//        // Calculate totals
//        BigDecimal subtotal = BigDecimal.ZERO;
//        List<InvoiceItem> items = new ArrayList<>();
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
//            items.add(item);
//            subtotal = subtotal.add(amount);
//        }
//
//        invoice.setSubtotal(subtotal);
//        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
//        invoice.setTaxAmount(taxAmount);
//        invoice.setTotalAmount(subtotal.add(taxAmount));
//        invoice.setItems(items);
//
//        return invoiceRepository.save(invoice);
//    }













    // Updated: Get all invoices (removed user filtering)
    public Page<Invoice> getInvoices(Authentication authentication, Pageable pageable) {
        // Authentication required but no user filtering
        return invoiceRepository.findAllInvoices(pageable);
    }

    // Updated: Get any invoice by ID (removed user ownership check)
    public Invoice getInvoiceById(Long id, Authentication authentication) {
        // Authentication required but no user ownership check
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    // Updated: Any authenticated user can update invoice status
    @Transactional
    public Invoice updateInvoiceStatus(Long id, InvoiceStatus status, Authentication authentication) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }

    // Updated: Get invoices with optional status filter (removed user filtering)
    public Page<Invoice> getInvoices(Authentication authentication, InvoiceStatus status, Pageable pageable) {
        if (status != null) {
            return invoiceRepository.findByStatus(status, pageable);
        }
        return invoiceRepository.findAllInvoices(pageable);
    }

    // Updated: Any authenticated user can delete any invoice
//    @Transactional
//    public void deleteInvoice(Long id, Authentication authentication) {
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        invoiceRepository.delete(invoice);
//    }

    // Updated: Any authenticated user can update any invoice
//    @Transactional
//    public Invoice updateInvoice(Long id, InvoiceRequest request, Authentication authentication) {
//        Invoice invoice = invoiceRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        // Check if invoice number already exists (excluding current invoice)
//        if (!request.getInvoiceNumber().equals(invoice.getInvoiceNumber()) &&
//                invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
//            throw new BadRequestException("Invoice number already exists");
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
//        // Set status if provided
//        if (request.getStatus() != null) {
//            invoice.setStatus(request.getStatus());
//        }
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
//        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
//        invoice.setTaxAmount(taxAmount);
//        invoice.setTotalAmount(subtotal.add(taxAmount));
//
//        return invoiceRepository.save(invoice);
//    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // New: Search invoices (removed user filtering)
    public Page<Invoice> searchInvoices(String search, Authentication authentication, Pageable pageable) {
        return invoiceRepository.findBySearch(search, pageable);
    }

    // New: Get overdue invoices
    public List<Invoice> getOverdueInvoices(Authentication authentication) {
        return invoiceRepository.findOverdueInvoices(LocalDate.now());
    }

    // New: Get invoices by date range
    public Page<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate, Authentication authentication, Pageable pageable) {
        return invoiceRepository.findByDateRange(startDate, endDate, pageable);
    }

    // New: Get invoices by client name
    public Page<Invoice> getInvoicesByClientName(String clientName, Authentication authentication, Pageable pageable) {
        return invoiceRepository.findByClientName(clientName, pageable);
    }

    // New: Get invoices by amount range
    public Page<Invoice> getInvoicesByAmountRange(BigDecimal minAmount, BigDecimal maxAmount, Authentication authentication, Pageable pageable) {
        return invoiceRepository.findByAmountRange(minAmount, maxAmount, pageable);
    }

    // New: Get invoice statistics
    public InvoiceStatsResponse getInvoiceStats(Authentication authentication) {
        long totalInvoices = invoiceRepository.count();
        long pendingInvoices = invoiceRepository.countByStatus(InvoiceStatus.PENDING);
        long paidInvoices = invoiceRepository.countByStatus(InvoiceStatus.PAID);
        long overdueInvoices = invoiceRepository.findOverdueInvoices(LocalDate.now()).size();

        BigDecimal totalPaidAmount = invoiceRepository.sumPaidInvoicesAmount();
        BigDecimal totalPendingAmount = invoiceRepository.sumPendingInvoicesAmount();
        BigDecimal totalOverdueAmount = invoiceRepository.sumOverdueInvoicesAmount(LocalDate.now());

        // Handle null values from database
        totalPaidAmount = totalPaidAmount != null ? totalPaidAmount : BigDecimal.ZERO;
        totalPendingAmount = totalPendingAmount != null ? totalPendingAmount : BigDecimal.ZERO;
        totalOverdueAmount = totalOverdueAmount != null ? totalOverdueAmount : BigDecimal.ZERO;

        InvoiceStatsResponse stats = new InvoiceStatsResponse();
        stats.setTotalInvoices(totalInvoices);
        stats.setPendingInvoices(pendingInvoices);
        stats.setPaidInvoices(paidInvoices);
        stats.setOverdueInvoices(overdueInvoices);
        stats.setTotalPaidAmount(totalPaidAmount);
        stats.setTotalPendingAmount(totalPendingAmount);
        stats.setTotalOverdueAmount(totalOverdueAmount);

        return stats;
    }

    // New: Get recent invoices (last 30 days)
    public Page<Invoice> getRecentInvoices(Authentication authentication, Pageable pageable) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return invoiceRepository.findRecentInvoices(thirtyDaysAgo, pageable);
    }

    // New: Get invoices created by specific user (for audit purposes)
    public Page<Invoice> getInvoicesCreatedByUser(Long userId, Authentication authentication, Pageable pageable) {
        return invoiceRepository.findInvoicesCreatedByUser(userId, pageable);
    }

    // New: Bulk update invoice status
    @Transactional
    public List<Invoice> bulkUpdateInvoiceStatus(List<Long> invoiceIds, InvoiceStatus status, Authentication authentication) {
        List<Invoice> updatedInvoices = new ArrayList<>();

        for (Long invoiceId : invoiceIds) {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + invoiceId));

            invoice.setStatus(status);
            updatedInvoices.add(invoiceRepository.save(invoice));
        }

        return updatedInvoices;
    }

    // New: Mark invoice as paid
    @Transactional
    public Invoice markAsPaid(Long id, Authentication authentication) {
        return updateInvoiceStatus(id, InvoiceStatus.PAID, authentication);
    }

    // New: Mark invoice as cancelled
    @Transactional
    public Invoice markAsCancelled(Long id, Authentication authentication) {
        return updateInvoiceStatus(id, InvoiceStatus.CANCELLED, authentication);
    }

    // New: Duplicate invoice
    @Transactional
    public Invoice duplicateInvoice(Long id, String newInvoiceNumber, Authentication authentication) {
        Invoice originalInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        // Check if new invoice number already exists
        if (invoiceRepository.existsByInvoiceNumber(newInvoiceNumber)) {
            throw new BadRequestException("Invoice number already exists");
        }

        User user = getCurrentUser(authentication);

        Invoice duplicatedInvoice = new Invoice();
        duplicatedInvoice.setInvoiceNumber(newInvoiceNumber);
        duplicatedInvoice.setClientName(originalInvoice.getClientName());
        duplicatedInvoice.setClientEmail(originalInvoice.getClientEmail());
        duplicatedInvoice.setClientAddress(originalInvoice.getClientAddress());
        duplicatedInvoice.setBusinessName(originalInvoice.getBusinessName());
        duplicatedInvoice.setBusinessEmail(originalInvoice.getBusinessEmail());
        duplicatedInvoice.setBusinessAddress(originalInvoice.getBusinessAddress());
        duplicatedInvoice.setInvoiceDate(LocalDate.now());
        duplicatedInvoice.setDueDate(LocalDate.now().plusDays(30)); // Default 30 days from now
        duplicatedInvoice.setTaxRate(originalInvoice.getTaxRate());
        duplicatedInvoice.setStatus(InvoiceStatus.PENDING);
        duplicatedInvoice.setUser(user);

        // Duplicate items
        List<InvoiceItem> duplicatedItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (InvoiceItem originalItem : originalInvoice.getItems()) {
            InvoiceItem duplicatedItem = new InvoiceItem();
            duplicatedItem.setDescription(originalItem.getDescription());
            duplicatedItem.setQuantity(originalItem.getQuantity());
            duplicatedItem.setRate(originalItem.getRate());
            duplicatedItem.setAmount(originalItem.getAmount());
            duplicatedItem.setInvoice(duplicatedInvoice);

            duplicatedItems.add(duplicatedItem);
            subtotal = subtotal.add(originalItem.getAmount());
        }

        duplicatedInvoice.setItems(duplicatedItems);
        duplicatedInvoice.setSubtotal(subtotal);
        duplicatedInvoice.setTaxAmount(originalInvoice.getTaxAmount());
        duplicatedInvoice.setTotalAmount(originalInvoice.getTotalAmount());

        return invoiceRepository.save(duplicatedInvoice);
    }








    // Update your existing InvoiceService class with these modifications

    @Transactional
    public Invoice createInvoice(InvoiceRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);

        // Check if invoice number already exists
        if (invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
            throw new BadRequestException("Invoice number already exists");
        }

        // Validate stock availability for inventory items
        inventoryService.validateStockAvailability(request.getItems());

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

        // Calculate totals and create items
        BigDecimal subtotal = BigDecimal.ZERO;
        List<InvoiceItem> items = new ArrayList<>();
        List<Long> inventoryItemIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        for (InvoiceItemRequest itemRequest : request.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemRequest.getDescription());
            item.setQuantity(itemRequest.getQuantity());
            item.setRate(itemRequest.getRate());
            BigDecimal amount = itemRequest.getRate().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            item.setAmount(amount);
            item.setInvoice(invoice);

            // Link to inventory item if provided
            if (itemRequest.getInventoryItemId() != null) {
                InventoryItem inventoryItem = inventoryService.getInventoryItemByIdInternal(
                        itemRequest.getInventoryItemId(), authentication);
                item.setInventoryItem(inventoryItem);

                // Collect inventory items to reduce quantities
                inventoryItemIds.add(itemRequest.getInventoryItemId());
                quantities.add(itemRequest.getQuantity());
            }

            items.add(item);
            subtotal = subtotal.add(amount);
        }

        invoice.setSubtotal(subtotal);
        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(subtotal.add(taxAmount));
        invoice.setItems(items);

        // Save invoice first
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Reduce inventory quantities for linked items
        if (!inventoryItemIds.isEmpty()) {
            inventoryService.bulkReduceInventoryQuantities(inventoryItemIds, quantities, authentication);
        }

        return savedInvoice;
    }

    @Transactional
    public void deleteInvoice(Long id, Authentication authentication) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        // Restore inventory quantities before deleting invoice
        List<Long> inventoryItemIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        for (InvoiceItem item : invoice.getItems()) {
            if (item.getInventoryItem() != null) {
                inventoryItemIds.add(item.getInventoryItem().getId());
                quantities.add(item.getQuantity());
            }
        }

        // Restore inventory quantities
        if (!inventoryItemIds.isEmpty()) {
            inventoryService.bulkRestoreInventoryQuantities(inventoryItemIds, quantities, authentication);
        }

        invoiceRepository.delete(invoice);
    }

    @Transactional
    public Invoice updateInvoice(Long id, InvoiceRequest request, Authentication authentication) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        // Check if invoice number already exists (excluding current invoice)
        if (!request.getInvoiceNumber().equals(invoice.getInvoiceNumber()) &&
                invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
            throw new BadRequestException("Invoice number already exists");
        }

        // First, restore inventory quantities from existing items
        List<Long> oldInventoryItemIds = new ArrayList<>();
        List<Integer> oldQuantities = new ArrayList<>();

        for (InvoiceItem item : invoice.getItems()) {
            if (item.getInventoryItem() != null) {
                oldInventoryItemIds.add(item.getInventoryItem().getId());
                oldQuantities.add(item.getQuantity());
            }
        }

        if (!oldInventoryItemIds.isEmpty()) {
            inventoryService.bulkRestoreInventoryQuantities(oldInventoryItemIds, oldQuantities, authentication);
        }

        // Validate stock availability for new items
        inventoryService.validateStockAvailability(request.getItems());

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

        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }

        // Clear existing items
        invoice.getItems().clear();

        // Create new items
        BigDecimal subtotal = BigDecimal.ZERO;
        List<InvoiceItem> updatedItems = new ArrayList<>();
        List<Long> newInventoryItemIds = new ArrayList<>();
        List<Integer> newQuantities = new ArrayList<>();

        for (InvoiceItemRequest itemRequest : request.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemRequest.getDescription());
            item.setQuantity(itemRequest.getQuantity());
            item.setRate(itemRequest.getRate());
            BigDecimal amount = itemRequest.getRate().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            item.setAmount(amount);
            item.setInvoice(invoice);

            // Link to inventory item if provided
            if (itemRequest.getInventoryItemId() != null) {
                InventoryItem inventoryItem = inventoryService.getInventoryItemByIdInternal(
                        itemRequest.getInventoryItemId(), authentication);
                item.setInventoryItem(inventoryItem);

                newInventoryItemIds.add(itemRequest.getInventoryItemId());
                newQuantities.add(itemRequest.getQuantity());
            }

            updatedItems.add(item);
            subtotal = subtotal.add(amount);
        }

        invoice.setItems(updatedItems);
        invoice.setSubtotal(subtotal);
        BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).divide(BigDecimal.valueOf(100));
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(subtotal.add(taxAmount));

        // Save updated invoice
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Reduce inventory quantities for new items
        if (!newInventoryItemIds.isEmpty()) {
            inventoryService.bulkReduceInventoryQuantities(newInventoryItemIds, newQuantities, authentication);
        }

        return savedInvoice;
    }

}
