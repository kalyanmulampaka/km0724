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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class RentalPricingServiceImplTest {
    @InjectMocks
    private RentalPricingServiceImpl pricingServiceUnderTest;

    private final Map<Integer, Tool> mockToolsMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        Tool chainsaw = Tool.builder().id(1)
                .toolCode(ToolCode.CHNS)
                .tooltype(ToolType.CHAINSAW)
                .brand(ToolBrand.STIHL.getName())
                .dailyRentalCharge(new BigDecimal("1.49"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(true)
                .build();
        mockToolsMap.put(chainsaw.getId(), chainsaw);
        Tool ladder = Tool.builder().id(2)
                .toolCode(ToolCode.LADW)
                .tooltype(ToolType.LADDER)
                .brand(ToolBrand.WERNER.getName())
                .dailyRentalCharge(new BigDecimal("1.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(true)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(ladder.getId(), ladder);
        Tool jackhammer1 = Tool.builder().id(3)
                .toolCode(ToolCode.JAKD)
                .tooltype(ToolType.JACKHAMMER)
                .brand(ToolBrand.DEWALT.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(jackhammer1.getId(), jackhammer1);

        Tool jackhammer2 = Tool.builder().id(4)
                .toolCode(ToolCode.JAKR)
                .tooltype(ToolType.JACKHAMMER)
                .brand(ToolBrand.RIDGID.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(jackhammer2.getId(), jackhammer2);
    }


    @Test
    public void testCalculateRentalCharge () throws Exception {
        LocalDate checkoutDate = LocalDate.of(2024, Month.AUGUST, 5);
        Cart cart = Cart.builder()
                .rentalDays(2)
                .toolIds(List.of(1,2))
                .discountPercent(10)
                .checkoutDate(checkoutDate).build();
        RentalItem rentalItem1 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(1)).build();
        RentalItem rentalItem2 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(2)).build();
        List<RentalItem> items = List.of (rentalItem1, rentalItem2);


        Rental expected = Rental.builder()
                .rentalDays(2)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .items(items)
                .preDiscountCharge(new BigDecimal(6.96).setScale(2, RoundingMode.CEILING))
                .discountAmount(new BigDecimal(0.70).setScale(2, RoundingMode.CEILING))
                .finalCharge(new BigDecimal(6.26).setScale(2, RoundingMode.CEILING))
                .build();

        Rental actual = this.pricingServiceUnderTest.calculateRentalCharge(cart, List.of(this.mockToolsMap.get(1), this.mockToolsMap.get(2)));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
