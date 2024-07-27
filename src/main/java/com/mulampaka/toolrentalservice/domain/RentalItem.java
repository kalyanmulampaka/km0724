package com.mulampaka.toolrentalservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents the rental line item
 */
@Data
@Builder
public class RentalItem {
    private Tool tool;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private Integer rentalDays;
    private Integer chargeDays;
    private BigDecimal preDiscountCharge;
    private Integer discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;


}
