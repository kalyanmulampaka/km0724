package com.mulampaka.toolrentalservice.service.impl;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;
import com.mulampaka.toolrentalservice.service.RentalPricingService;
import com.mulampaka.toolrentalservice.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class RentalPricingServiceImpl implements RentalPricingService {

    /**
     * Returns the rental charge per day for the specified cart and chosen tools.
     * Computes the charge based on the checkout date falls on a weekday, weekend or holiday
     * Calculates the discount and deducts from rental charge to get the final rental charge
     * @param cart Cart
     * @param tools Collection<Tool>
     * @throws ToolRentalException for validation errors
     */
    public Rental calculateRentalCharge(Cart cart, Collection<Tool> tools) {
        Rental rental = Rental.builder().build();
        LocalDate checkoutDate = cart.getCheckoutDate();
        rental.setCheckoutDate(checkoutDate);
        Integer rentalDays = cart.getRentalDays();
        rental.setRentalDays(rentalDays);
        LocalDate dueDate = rental.getCheckoutDate().plusDays(rental.getRentalDays()-1);
        rental.setDueDate(dueDate);
        BigDecimal totalRentalCharge = new BigDecimal("0");
        List<RentalItem> items = new ArrayList<>();
        rental.setItems(items);
        /**
         * For each tool
         *   For each rental day
         *     calculate the rental charge per day
         *   store the number of charge days
         *   calculate the rental charge before discount by adding the rental charge of each tool
         *   calculate the discount amount = rental charge * (discount percent/100)
         *   calculate the final rental charge = rental charge - discount amount
         */
        int rentalChargeDays = 0;
        LocalDate rentalDay = checkoutDate;
        for (Tool tool : tools) {
            log.debug("Calculating Rental Charge for Tool:Id:{}, Tool Code:{}", tool.getId(), tool.getToolCode());
            RentalItem item = RentalItem.builder().build();
            items.add(item);
            item.setTool(tool);
            for (int i=1; i <= rentalDays; i++) {
                BigDecimal rentalChargeOfDay = this.calculateRentalChargePerDay (rentalDay, tool);
                log.debug("Rental Charge for Day:{}, Date:{}, Amount:{}", i, rentalDay, rentalChargeOfDay);
                if (rentalChargeOfDay.compareTo(BigDecimal.ZERO) == 1) {
                    rentalChargeDays++;
                }
                totalRentalCharge = totalRentalCharge.add(rentalChargeOfDay);
                rentalDay = rentalDay.plusDays(1);
            }
            item.setChargeDays(rentalChargeDays);
            log.debug("Rental Item:{}", item);
            // reset these to calculate for the next tool
            rentalChargeDays = 0;
            rentalDay = checkoutDate;
        }
        log.debug("PreDiscount Rental Charge:{}", totalRentalCharge);
        rental.setPreDiscountCharge(totalRentalCharge);
        rental.setDiscountPercent(cart.getDiscountPercent());
        this.calculateDiscount(rental);
        return rental;
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

    private void calculateDiscount (Rental rental) {
        Integer discountPercent = rental.getDiscountPercent();
        if (discountPercent != null && (discountPercent < 0 || discountPercent > 100)) {
            throw new ToolRentalException("Invalid Discount Percent:" + discountPercent + ". Valid Discount Percent Range:0-100");
        } else {
            log.debug("Discount Percent:{}", discountPercent);
            double discPercent = (double) discountPercent/100;
            BigDecimal discountAmount = rental.getPreDiscountCharge().multiply(BigDecimal.valueOf(discPercent)).setScale(2, RoundingMode.CEILING);
            log.debug("Discount Amount:{}", discountAmount);
            rental.setDiscountAmount(discountAmount);
            rental.setFinalCharge(rental.getPreDiscountCharge().subtract(discountAmount).setScale(2, RoundingMode.CEILING));
            log.debug("Final Rental Charge:{}", rental.getFinalCharge());

        }
    }
}
