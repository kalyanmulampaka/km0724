package com.mulampaka.toolrentalservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents the Tool entity
 */
@Data
@Builder
public class Tool {
    private Integer id;
    private ToolCode toolCode;
    private ToolType tooltype;
    private String brand;
    private BigDecimal dailyRentalCharge;
    private boolean isWeekdayCharged;
    private boolean isWeekendCharged;
    private boolean isHolidayCharged;

}
