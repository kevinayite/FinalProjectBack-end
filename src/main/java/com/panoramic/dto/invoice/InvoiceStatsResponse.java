package com.panoramic.dto.invoice;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InvoiceStatsResponse {
    private long totalInvoices;
    private long pendingInvoices;
    private long paidInvoices;
    private long overdueInvoices;
    private BigDecimal totalPaidAmount;
    private BigDecimal totalPendingAmount;
    private BigDecimal totalOverdueAmount;
}
