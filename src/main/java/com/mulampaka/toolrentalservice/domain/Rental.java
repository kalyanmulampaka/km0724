package com.mulampaka.toolrentalservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents the Rental of a tool
 * Stores the Tool details and days charged details for each tool entity after the charge is calculated
 */
@Data
@Builder
public class Rental {
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private Integer rentalDays;
    private List<RentalItem> items;
    private BigDecimal preDiscountCharge;
    private Integer discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;
}
