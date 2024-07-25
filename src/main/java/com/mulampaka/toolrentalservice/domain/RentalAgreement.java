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
    private Integer id;
    private Rental rental;



    public void print () {
        printLine("========================= RENTAL AGREEMENT =========================");
        printLine("Rental Agreement ID\t: " + this.id);
        printLine("Check out Date\t\t: " + rental.getCheckoutDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        printLine("Due Date\t\t\t: " + rental.getDueDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        printLine("Total Rental Days\t: " + rental.getRentalDays());
        printLine("------------ Rented Tools ------------");
        int i = 1;
        for (RentalItem item : rental.getItems()) {
            printLine("\t(" + i++ + ") Tool Code\t\t\t: " + item.getTool().getToolCode());
            printLine("\t\tTool Type\t\t\t: " + item.getTool().getToolType());
            printLine("\t\tTool Brand\t\t\t: " + item.getTool().getBrand());
            printLine("\t\tDaily Rental Charge\t: " + this.formatCurrency(item.getTool().getDailyRentalCharge()));
            printLine("\t\tNo of Days Charged\t: " + item.getChargeDays());
        }
        printLine("--------------------------------------");
        printLine("Discount Percentage\t\t\t: " + rental.getDiscountPercent() + "%");
        printLine("Pre-Discount Rental Charge\t: " + this.formatCurrency(rental.getPreDiscountCharge()));
        printLine("(-) Discount Amount\t\t\t: " + this.formatCurrency(rental.getDiscountAmount()));
        printLine("--------------------------------------");
        printLine("Final Rental Charge\t\t\t: " + this.formatCurrency(rental.getFinalCharge()));
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
