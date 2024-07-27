package com.mulampaka.toolrentalservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

/**
 * Represents each item in a cart
 */
@Data
@Builder
@Jacksonized
public class CartItem {
    @JsonFormat(pattern = "MM-dd-yyyy", timezone = Constants.DEFAULT_TIMEZONE)
    private LocalDate checkoutDate;
    private String toolCode;
    private Integer rentalDays;
    private Integer discountPercent;
}
