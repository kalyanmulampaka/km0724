package com.mulampaka.toolrentalservice.test.service;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.service.impl.RentalPricingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class RentalPricingServiceImplTest {
    @InjectMocks
    private RentalPricingServiceImpl pricingServiceUnderTest;

    private final Map<ToolCode, Tool> mockToolsMap = new HashMap<>();


    @BeforeEach
    void setUp() {
        Tool chainsaw = Tool.builder().id(1)
                .toolCode(ToolCode.CHNS)
                .toolType(ToolType.CHAINSAW)
                .brand(ToolBrand.STIHL.getName())
                .dailyRentalCharge(new BigDecimal("1.49"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(true)
                .build();
        mockToolsMap.put(chainsaw.getToolCode(), chainsaw);

        Tool ladder = Tool.builder().id(2)
                .toolCode(ToolCode.LADW)
                .toolType(ToolType.LADDER)
                .brand(ToolBrand.WERNER.getName())
                .dailyRentalCharge(new BigDecimal("1.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(true)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(ladder.getToolCode(), ladder);

        Tool jackhammer1 = Tool.builder().id(3)
                .toolCode(ToolCode.JAKD)
                .toolType(ToolType.JACKHAMMER)
                .brand(ToolBrand.DEWALT.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(jackhammer1.getToolCode(), jackhammer1);

        Tool jackhammer2 = Tool.builder().id(4)
                .toolCode(ToolCode.JAKR)
                .toolType(ToolType.JACKHAMMER)
                .brand(ToolBrand.RIDGID.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(jackhammer2.getToolCode(), jackhammer2);
    }


    @Test
    public void testCalculateRentalCharge () throws Exception {
        LocalDate checkoutDate = LocalDate.of(2024, Month.AUGUST, 5);

        CartItem item1 = CartItem.builder()
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .rentalDays(2)
                .toolCode(ToolCode.CHNS.name())
                .build();

        BigDecimal preDiscountCharge = new BigDecimal(2.98).setScale(2, RoundingMode.CEILING);
        BigDecimal discountAmount = new BigDecimal(0.30).setScale(2, RoundingMode.CEILING);
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.CEILING);
        RentalItem expected = RentalItem.builder()
                .tool(this.mockToolsMap.get(ToolCode.CHNS))
                .rentalDays(2)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .chargeDays(2)
                .preDiscountCharge(preDiscountCharge)
                .discountAmount(discountAmount)
                .finalCharge(finalCharge)
                .build();

        RentalItem actual = this.pricingServiceUnderTest.calculateRentalCharge(item1, this.mockToolsMap.get(ToolCode.CHNS));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
