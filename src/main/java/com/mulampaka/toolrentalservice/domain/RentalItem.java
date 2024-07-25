package com.mulampaka.toolrentalservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents the rental line item
 */
@Data
@Builder
public class RentalItem {
    private Tool tool;
    private Integer chargeDays;

}
