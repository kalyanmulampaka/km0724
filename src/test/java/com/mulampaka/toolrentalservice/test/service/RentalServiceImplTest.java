package com.mulampaka.toolrentalservice.test.service;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.service.RentalPricingService;
import com.mulampaka.toolrentalservice.service.ToolInventoryService;
import com.mulampaka.toolrentalservice.service.impl.RentalServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Slf4j
public class RentalServiceImplTest {
    @InjectMocks
    private RentalServiceImpl rentalServiceUnderTest;

    @Mock
    private RentalPricingService pricingService;

    @Mock
    private ToolInventoryService toolInventoryService;

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
    public void testGetAvailableTools () throws Exception {
        when(this.toolInventoryService.getAvailableTools(null)).thenReturn(this.mockToolsMap.values());
        Collection<Tool> tools = this.rentalServiceUnderTest.getAvailableTools(null);
        assertNotNull(tools);
        assertTrue(tools.size() == 4);
    }

    @Test
    public void testCheckoutWeekdays () throws Exception {
        LocalDate checkoutDate = LocalDate.of(2024, Month.SEPTEMBER, 18);
        List<CartItem> cartItems = new ArrayList<>();
        Cart cart = Cart.builder()
                .cartItems(cartItems).build();
        CartItem item1 = CartItem.builder().checkoutDate(checkoutDate)
                .discountPercent(10)
                .rentalDays(2)
                .toolCode(ToolCode.CHNS.name())
                .build();
        cartItems.add(item1);
        CartItem item2 = CartItem.builder().checkoutDate(checkoutDate)
                .discountPercent(10)
                .rentalDays(2)
                .toolCode(ToolCode.LADW.name())
                .build();
        cartItems.add(item2);


        Tool chns = this.mockToolsMap.get(1);
        Tool ladw = this.mockToolsMap.get(2);
        RentalItem rentalItem1 = RentalItem.builder()
                .tool(this.mockToolsMap.get(ToolCode.CHNS))
                .rentalDays(2)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .preDiscountCharge(new BigDecimal(6.96).setScale(2, RoundingMode.CEILING))
                .discountAmount(new BigDecimal(0.70).setScale(2, RoundingMode.CEILING))
                .finalCharge(new BigDecimal(6.26).setScale(2, RoundingMode.CEILING))
                .build();
        RentalItem rentalItem2 = RentalItem.builder()
                .tool(this.mockToolsMap.get(ToolCode.LADW))
                .rentalDays(2)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .preDiscountCharge(new BigDecimal(6.96).setScale(2, RoundingMode.CEILING))
                .discountAmount(new BigDecimal(0.70).setScale(2, RoundingMode.CEILING))
                .finalCharge(new BigDecimal(6.26).setScale(2, RoundingMode.CEILING))
                .build();

        when(this.toolInventoryService.getToolByCode(ToolCode.CHNS.name())).thenReturn(chns);
        when(this.toolInventoryService.getToolByCode(ToolCode.LADW.name())).thenReturn(ladw);
        when(this.pricingService.calculateRentalCharge(item1, chns)).thenReturn(rentalItem1);
        when(this.pricingService.calculateRentalCharge(item2, ladw)).thenReturn(rentalItem2);
        List<RentalAgreement> expectedRentalAgreements = new ArrayList<>();
        RentalAgreement expectedItem1 = RentalAgreement.builder().id(2).rentalItem(rentalItem1).build();
        RentalAgreement expectedItem2 = RentalAgreement.builder().id(3).rentalItem(rentalItem2).build();
        expectedRentalAgreements.add(expectedItem1);
        expectedRentalAgreements.add(expectedItem2);
        List<RentalAgreement> actual = this.rentalServiceUnderTest.checkout(cart);
        assertNotNull(actual);
        assertEquals(expectedRentalAgreements, actual);
    }


    @Test
    public void testCheckoutIndependenceDay () throws Exception {
        LocalDate checkoutDate = LocalDate.of(2025, Month.JULY, 4);
        List<CartItem> cartItems = new ArrayList<>();
        Cart cart = Cart.builder()
                .cartItems(cartItems).build();
        CartItem item1 = CartItem.builder().checkoutDate(checkoutDate)
                .discountPercent(10)
                .rentalDays(2)
                .toolCode(ToolCode.LADW.name())
                .build();
        cartItems.add(item1);

        Tool ladw = this.mockToolsMap.get(2);
        RentalItem rentalItemLadw = RentalItem.builder()
                .tool(this.mockToolsMap.get(ToolCode.LADW))
                .rentalDays(2)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .preDiscountCharge(new BigDecimal(0.00))
                .discountAmount(new BigDecimal(0.00))
                .finalCharge(new BigDecimal(0.00))
                .build();
        when(this.toolInventoryService.getToolByCode(ToolCode.LADW.name())).thenReturn(ladw);
        when(this.pricingService.calculateRentalCharge(item1, ladw)).thenReturn(rentalItemLadw);
        List<RentalAgreement> expectedRentalAgreements = new ArrayList<>();
        RentalAgreement expected = RentalAgreement.builder().id(4).rentalItem(rentalItemLadw).build();
        expectedRentalAgreements.add(expected);
        List<RentalAgreement> actual = this.rentalServiceUnderTest.checkout(cart);
        assertNotNull(actual);
        assertEquals(expectedRentalAgreements, actual);
    }

    @Test
    public void testCheckoutLaborDay () throws Exception {
        LocalDate checkoutDate = LocalDate.of(2024, Month.SEPTEMBER, 2);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem item1 = CartItem.builder()
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .rentalDays(1)
                .toolCode(ToolCode.LADW.name())
                .build();
        cartItems.add(item1);
        Cart cart = Cart.builder()
                .cartItems(cartItems).build();

        Tool ladw = this.mockToolsMap.get(2);
        RentalItem rentalItemLadw = RentalItem.builder()
                .tool(this.mockToolsMap.get(ToolCode.LADW))
                .rentalDays(1)
                .checkoutDate(checkoutDate)
                .discountPercent(10)
                .dueDate(checkoutDate.plusDays(1))
                .preDiscountCharge(new BigDecimal(0.00))
                .discountAmount(new BigDecimal(0.00))
                .finalCharge(new BigDecimal(0.00))
                .build();
        when(this.toolInventoryService.getToolByCode(ToolCode.LADW.name())).thenReturn(ladw);
        when(this.pricingService.calculateRentalCharge(item1, ladw)).thenReturn(rentalItemLadw);

        List<RentalAgreement> expectedRentalAgreements = new ArrayList<>();
        RentalAgreement  expected = RentalAgreement.builder().id(1).rentalItem(rentalItemLadw).build();
        expected.print();
        expectedRentalAgreements.add(expected);
        List<RentalAgreement>  actual = this.rentalServiceUnderTest.checkout(cart);
        actual.get(0).print();
        assertNotNull(actual);
        assertEquals(expectedRentalAgreements, actual);
    }

}
