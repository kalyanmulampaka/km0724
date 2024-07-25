package com.mulampaka.toolrentalservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents the Rental Agreement entity
 */
@Data
@Builder
public class RentalAgreement {
    private Integer id;
    private Rental rental;



    public void print () {
        System.out.println(this.toString());
    }
}
