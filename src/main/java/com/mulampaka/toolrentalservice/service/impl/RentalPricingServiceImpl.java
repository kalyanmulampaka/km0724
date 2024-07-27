package com.mulampaka.toolrentalservice.service.impl;

import com.mulampaka.toolrentalservice.domain.CartItem;
import com.mulampaka.toolrentalservice.domain.RentalDay;
import com.mulampaka.toolrentalservice.domain.RentalItem;
import com.mulampaka.toolrentalservice.domain.Tool;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;
import com.mulampaka.toolrentalservice.service.RentalPricingService;
import com.mulampaka.toolrentalservice.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@Slf4j
public class RentalPricingServiceImpl implements RentalPricingService {

    /**
     * Returns the rental charge per day for the specified cart item and chosen tool.
     * Computes the charge based on the checkout date falls on a weekday, weekend or holiday
     * Calculates the discount and deducts from rental charge to get the final rental charge
     * @param cartItem CartItem
     * @param tool Tool
     * @throws ToolRentalException for validation errors
     */
    public RentalItem calculateRentalCharge(CartItem cartItem, Tool tool) {
        RentalItem rentalItem = RentalItem.builder().tool(tool).build();
        LocalDate checkoutDate = cartItem.getCheckoutDate();
        rentalItem.setCheckoutDate(checkoutDate);
        Integer rentalDays = cartItem.getRentalDays();
        rentalItem.setRentalDays(rentalDays);
        LocalDate dueDate = rentalItem.getCheckoutDate().plusDays(rentalItem.getRentalDays()-1);
        rentalItem.setDueDate(dueDate);
        BigDecimal totalRentalCharge = new BigDecimal("0");
        /**
         * For the specified tool
         *   For each rental day
         *     calculate the rental charge per day
         *   store the number of charge days
         *   calculate the rental charge before discount by adding the rental charge of the tool
         *   calculate the discount amount = rental charge * (discount percent/100)
         *   calculate the final rental charge = rental charge - discount amount
         */
        int rentalChargeDays = 0;
        LocalDate rentalDay = checkoutDate;
        log.debug("Calculating Rental Charge for Tool:Id:{}, Tool Code:{}", tool.getId(), tool.getToolCode());
        for (int i = 1; i <= rentalDays; i++) {
            BigDecimal rentalChargeOfDay = this.calculateRentalChargePerDay(rentalDay, tool);
            log.debug("Rental Charge for Day:{}, Date:{}, Amount:{}", i, rentalDay, rentalChargeOfDay);
            //if rentalCharge of the day is greater than zero, increment rental charge days
            if (rentalChargeOfDay.compareTo(BigDecimal.ZERO) == 1) {
                rentalChargeDays++;
            }
            totalRentalCharge = totalRentalCharge.add(rentalChargeOfDay);
            rentalDay = rentalDay.plusDays(1);
        }
        rentalItem.setChargeDays(rentalChargeDays);
        log.debug("Rental Item:{}", rentalItem);
        log.debug("PreDiscount Rental Charge:{}", totalRentalCharge);
        rentalItem.setPreDiscountCharge(totalRentalCharge.setScale(2, RoundingMode.CEILING));
        rentalItem.setDiscountPercent(cartItem.getDiscountPercent());
        this.calculateDiscount(rentalItem);
        return rentalItem;
    }


    private BigDecimal calculateRentalChargePerDay(LocalDate day, Tool tool) {
        RentalDay rentalDay = checkDay(day);
        log.debug("Day is {}", rentalDay);
        BigDecimal rentalCharge = new BigDecimal("0");
        switch (rentalDay) {
            case WEEKDAY -> {
                if (tool.isWeekdayCharged()) {
                    rentalCharge = rentalCharge.add(tool.getDailyRentalCharge());
                }
            }
            case WEEKEND -> {
                if (tool.isWeekendCharged()) {
                    rentalCharge = rentalCharge.add(tool.getDailyRentalCharge());
                }
            }
            case HOLIDAY -> {
                if (tool.isHolidayCharged()) {
                    rentalCharge = rentalCharge.add(tool.getDailyRentalCharge());
                }
            }
        }
        return rentalCharge;
    }


    private RentalDay checkDay(LocalDate day) {
        log.debug("Checking rental day:{}", day);
        if (DateUtil.isWeekend(day))
            return RentalDay.WEEKEND;
        if (DateUtil.isHoliday(day))
            return RentalDay.HOLIDAY;
        return RentalDay.WEEKDAY;
    }

    private void calculateDiscount (RentalItem rentalItem) {
        Integer discountPercent = rentalItem.getDiscountPercent();
        if (discountPercent != null && (discountPercent < 0 || discountPercent > 100)) {
            throw new ToolRentalException("Invalid Discount Percent:" + discountPercent + ". Valid Discount Percent Range:0-100");
        } else {
            log.debug("Discount Percent:{}", discountPercent);
            double discPercent = (double) discountPercent/100;
            BigDecimal discountAmount = rentalItem.getPreDiscountCharge().multiply(BigDecimal.valueOf(discPercent)).setScale(2, RoundingMode.CEILING);
            log.debug("Discount Amount:{}", discountAmount);
            rentalItem.setDiscountAmount(discountAmount.setScale(2, RoundingMode.CEILING));
            BigDecimal finalCharge = rentalItem.getPreDiscountCharge().subtract(discountAmount);//.setScale(2, RoundingMode.CEILING);
            rentalItem.setFinalCharge(finalCharge.setScale(2, RoundingMode.CEILING));
            log.debug("Final Rental Charge:{}", rentalItem.getFinalCharge());
        }
    }
}
