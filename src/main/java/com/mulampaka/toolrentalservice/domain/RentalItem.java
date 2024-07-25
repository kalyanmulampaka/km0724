package com.mulampaka.toolrentalservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RentalItem {
    private Tool tool;
    private Integer chargeDays;

}
