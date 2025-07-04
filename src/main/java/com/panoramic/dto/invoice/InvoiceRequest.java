package com.panoramic.dto.invoice;

import com.panoramic.model.enums.InvoiceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceRequest {
    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @NotBlank(message = "Client name is required")
    private String clientName;
    private InvoiceStatus status;

    private String clientEmail;
    private String clientAddress;
    private String businessName;
    private String businessEmail;
    private String businessAddress;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private BigDecimal taxRate;

    @NotEmpty(message = "Invoice items are required")
    private List<InvoiceItemRequest> items;
}
