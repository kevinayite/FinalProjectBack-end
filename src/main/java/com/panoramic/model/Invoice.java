//package com.panoramic.model;
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import com.panoramic.model.enums.InvoiceStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "invoices")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//public class Invoice {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "invoice_number", unique = true, nullable = false)
//    private String invoiceNumber;
//
//    @Column(name = "client_name", nullable = false)
//    private String clientName;
//
//    @Column(name = "client_email")
//    private String clientEmail;
//
//    @Column(name = "client_address")
//    private String clientAddress;
//
//    @Column(name = "business_name")
//    private String businessName;
//
//    @Column(name = "business_email")
//    private String businessEmail;
//
//    @Column(name = "business_address")
//    private String businessAddress;
//
//    @Column(name = "invoice_date", nullable = false)
//    private LocalDate invoiceDate;
//
//    @Column(name = "due_date")
//    private LocalDate dueDate;
//
//    @Column(name = "subtotal", nullable = false)
//    private BigDecimal subtotal;
//
//    @Column(name = "tax_rate")
//    private BigDecimal taxRate;
//
//    @Column(name = "tax_amount")
//    private BigDecimal taxAmount;
//
//    @Column(name = "total_amount", nullable = false)
//    private BigDecimal totalAmount;
//
//    @Enumerated(EnumType.STRING)
//    private InvoiceStatus status;
//
//    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<InvoiceItem> items;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @CreatedDate
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//}


package com.panoramic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.panoramic.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "client_address")
    private String clientAddress;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_email")
    private String businessEmail;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("invoice-items")
    private List<InvoiceItem> items;

    // FIXED: Add JsonIgnore to prevent circular reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
