package com.mulampaka.toolrentalservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents the shopping cart entity
 */
@Data
@Builder
@Jacksonized
public class Cart {

    @JsonFormat(pattern = "MM-dd-yyyy", timezone = Constants.DEFAULT_TIMEZONE)
    private LocalDate checkoutDate;
    private List<String> toolCodes;
    private Integer rentalDays;
    private Integer discountPercent;
}
