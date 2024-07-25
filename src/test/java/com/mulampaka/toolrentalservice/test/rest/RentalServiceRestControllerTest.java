package com.mulampaka.toolrentalservice.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.rest.RentalServiceRestController;
import com.mulampaka.toolrentalservice.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentalServiceRestController.class)
public class RentalServiceRestControllerTest {
    private static final String API_BASE_URL = "/api";

    @MockBean
    private RentalService rentalService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final Map<ToolCode, Tool> mockToolsMap = new HashMap<>();

    @BeforeEach
    public void setup() {
        Tool chainsaw = Tool.builder().id(1)
                .toolCode(ToolCode.CHNS)
                .tooltype(ToolType.CHAINSAW)
                .brand(ToolBrand.STIHL.getName())
                .dailyRentalCharge(new BigDecimal("1.49"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(true)
                .build();
        mockToolsMap.put(chainsaw.getToolCode(), chainsaw);

        Tool ladder = Tool.builder().id(2)
                .toolCode(ToolCode.LADW)
                .tooltype(ToolType.LADDER)
                .brand(ToolBrand.WERNER.getName())
                .dailyRentalCharge(new BigDecimal("1.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(true)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(ladder.getToolCode(), ladder);

        Tool jackhammer1 = Tool.builder().id(3)
                .toolCode(ToolCode.JAKD)
                .tooltype(ToolType.JACKHAMMER)
                .brand(ToolBrand.DEWALT.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(jackhammer1.getToolCode(), jackhammer1);

        Tool jackhammer2 = Tool.builder().id(4)
                .toolCode(ToolCode.JAKR)
                .tooltype(ToolType.JACKHAMMER)
                .brand(ToolBrand.RIDGID.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        mockToolsMap.put(jackhammer2.getToolCode(), jackhammer2);
    }

    @Test
    void testHealthApi() throws Exception {
        mockMvc.perform(get(API_BASE_URL + "/health")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("Tool Rental Server is UP."));
    }

    @Test
    void testGetAvailableToolsApi() throws Exception {

        when(this.rentalService.getAvailableTools(null)).thenReturn(this.mockToolsMap.values());
        mockMvc.perform(get(API_BASE_URL + "/tools")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(this.objectMapper.writeValueAsString(this.mockToolsMap.values())));
    }

    @Test
    void testCheckoutApi() throws Exception {
        List<String> toolCodes = List.of(ToolCode.CHNS.name(), ToolCode.LADW.name());
        LocalDate checkoutDate = LocalDate.of(2024, Month.SEPTEMBER, 10);
        Cart cart = Cart.builder()
                .rentalDays(2)
                .toolCodes(toolCodes)
                .discountPercent(10)
                .checkoutDate(checkoutDate).build();


        RentalItem rentalItem1 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(ToolCode.CHNS)).build();
        RentalItem rentalItem2 = RentalItem.builder().chargeDays(2).tool(this.mockToolsMap.get(ToolCode.LADW)).build();
        List<RentalItem> items = List.of(rentalItem1, rentalItem2);
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

        RentalAgreement rentalAgreement = RentalAgreement.builder().id(1).rental(rental).build();
        when(this.rentalService.checkout(cart)).thenReturn(rentalAgreement);

        mockMvc.perform(post(API_BASE_URL + "/checkout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(content().string(this.objectMapper.writeValueAsString(rentalAgreement)));


    }

}
