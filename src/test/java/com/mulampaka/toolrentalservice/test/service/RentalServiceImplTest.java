package com.mulampaka.toolrentalservice.test.service;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.service.RentalPricingService;
import com.mulampaka.toolrentalservice.service.ToolInventoryService;
import com.mulampaka.toolrentalservice.service.impl.RentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class RentalServiceImplTest {
    @InjectMocks
    private RentalServiceImpl rentalServiceUnderTest;

    @Mock
    private RentalPricingService pricingService;

    @Mock
    private ToolInventoryService toolInventoryService;

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
    public void testGetAvailableTools () throws Exception {
        when(this.toolInventoryService.getAvailableTools(null)).thenReturn(this.mockToolsMap.values());
        Collection<Tool> tools = this.rentalServiceUnderTest.getAvailableTools(null);
        assertNotNull(tools);
        assertTrue(tools.size() == 4);
    }

    @Test
    public void testCheckoutWeekdays () throws Exception {
        List<Tool> tools = List.of (this.mockToolsMap.get(1), this.mockToolsMap.get(2));
        LocalDate checkoutDate = LocalDate.of(2024, Month.SEPTEMBER, 18);
        Cart cart = Cart.builder()
                .rentalDays(2)
                .toolCodes(List.of(ToolCode.CHNS.name(), ToolCode.LADW.name()))
                .discountPercent(10)
                .checkoutDate(checkoutDate).build();
        RentalItem rentalItem1 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(1)).build();
        RentalItem rentalItem2 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(2)).build();
        List<RentalItem> items = List.of (rentalItem1, rentalItem2);


        Rental rental = Rental.builder()
                .rentalDays(2)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .items(items)
                .preDiscountCharge(new BigDecimal(6.96))
                .discountAmount(new BigDecimal(0.70))
                .finalCharge(new BigDecimal(6.26))
                .build();

        when(this.toolInventoryService.getToolsByCodes(List.of(ToolCode.CHNS.name(), ToolCode.LADW.name()))).thenReturn(tools);
        when(this.pricingService.calculateRentalCharge(cart, tools)).thenReturn(rental);
        RentalAgreement expected = RentalAgreement.builder().id(1).rental(rental).build();
        RentalAgreement actual = this.rentalServiceUnderTest.checkout(cart);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }


    @Test
    public void testCheckoutIndependenceDay () throws Exception {
        List<Tool> tools = List.of (this.mockToolsMap.get(2));
        LocalDate checkoutDate = LocalDate.of(2025, Month.JULY, 4);
        Cart cart = Cart.builder()
                .rentalDays(1)
                .toolCodes(List.of(ToolCode.LADW.name()))
                .discountPercent(10)
                .checkoutDate(checkoutDate).build();
        RentalItem rentalItem2 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(2)).build();
        List<RentalItem> items = List.of (rentalItem2);


        Rental rental = Rental.builder()
                .rentalDays(1)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .items(items)
                .preDiscountCharge(new BigDecimal(0.00))
                .discountAmount(new BigDecimal(0.00))
                .finalCharge(new BigDecimal(0.00))
                .build();

        when(this.toolInventoryService.getToolsByCodes(List.of(ToolCode.LADW.name()))).thenReturn(tools);
        when(this.pricingService.calculateRentalCharge(cart, tools)).thenReturn(rental);
        RentalAgreement expected = RentalAgreement.builder().id(1).rental(rental).build();
        RentalAgreement actual = this.rentalServiceUnderTest.checkout(cart);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckoutLaborDay () throws Exception {
        List<Tool> tools = List.of (this.mockToolsMap.get(2));
        LocalDate checkoutDate = LocalDate.of(2024, Month.SEPTEMBER, 2);
        Cart cart = Cart.builder()
                .rentalDays(1)
                .toolCodes(List.of(ToolCode.LADW.name()))
                .discountPercent(10)
                .checkoutDate(checkoutDate).build();

        RentalItem rentalItem2 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(2)).build();
        List<RentalItem> items = List.of (rentalItem2);


        Rental rental = Rental.builder()
                .rentalDays(1)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .items(items)
                .preDiscountCharge(new BigDecimal(0.00))
                .discountAmount(new BigDecimal(0.00))
                .finalCharge(new BigDecimal(0.00))
                .build();

        when(this.toolInventoryService.getToolsByCodes(List.of(ToolCode.LADW.name()))).thenReturn(tools);
        when(this.pricingService.calculateRentalCharge(cart, tools)).thenReturn(rental);
        RentalAgreement expected = RentalAgreement.builder().id(1).rental(rental).build();
        RentalAgreement actual = this.rentalServiceUnderTest.checkout(cart);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}
