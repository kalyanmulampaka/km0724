package com.mulampaka.toolrentalservice.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

/**
 * Represents the Rental Agreement entity
 */
@Data
@Builder
public class RentalAgreement {
    private static Integer ID = 0;
    private Integer id;
    private RentalItem rentalItem;

    /**
     * Hardcoded for assessment. This id will be the database id from RentalAgreement table
     */
    public void nextId () {
        this.id = ++ID;
    }


    public void print () {
        printLine("========================= RENTAL AGREEMENT =========================");
        printLine("Rental Agreement ID\t\t\t: " + this.id);
        printLine("Check out Date\t\t\t\t: " + rentalItem.getCheckoutDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        printLine("Due Date\t\t\t\t\t: " + rentalItem.getDueDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        printLine("Total Rental Days\t\t\t: " + rentalItem.getRentalDays());
        printLine("Tool Code\t\t\t\t\t: " + rentalItem.getTool().getToolCode());
        printLine("Tool Type\t\t\t\t\t: " + rentalItem.getTool().getToolType());
        printLine("Tool Brand\t\t\t\t\t: " + rentalItem.getTool().getBrand());
        printLine("Daily Rental Charge\t\t\t: " + this.formatCurrency(rentalItem.getTool().getDailyRentalCharge()));
        printLine("No of Days Charged\t\t\t: " + rentalItem.getChargeDays());
        printLine("Discount Percentage\t\t\t: " + rentalItem.getDiscountPercent() + "%");
        printLine("Pre-Discount Rental Charge\t: " + this.formatCurrency(rentalItem.getPreDiscountCharge()));
        printLine("(-) Discount Amount\t\t\t: " + this.formatCurrency(rentalItem.getDiscountAmount()));
        printLine("--------------------------------------------------------------------");
        printLine("Final Rental Charge\t\t\t: " + this.formatCurrency(rentalItem.getFinalCharge()));
        printLine("====================================================================");
    }

    private void printLine(String str) {
        System.out.println(str);
    }

    private String formatCurrency (BigDecimal amount) {
        StringBuffer buf = new StringBuffer("$");
        buf.append(amount.setScale(2, RoundingMode.CEILING));
        return buf.toString();
    }
}
