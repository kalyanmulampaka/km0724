package com.mulampaka.toolrentalservice.test.service;

import com.mulampaka.toolrentalservice.domain.Tool;
import com.mulampaka.toolrentalservice.domain.ToolBrand;
import com.mulampaka.toolrentalservice.domain.ToolCode;
import com.mulampaka.toolrentalservice.domain.ToolType;
import com.mulampaka.toolrentalservice.repository.ToolInventoryRepository;
import com.mulampaka.toolrentalservice.service.impl.ToolInventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class ToolInventoryServiceImplTest {
    @InjectMocks
    private ToolInventoryServiceImpl toolInventoryServiceUnderTest;

    @MockBean
    private ToolInventoryRepository toolInventoryRepository;


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
        when(this.toolInventoryRepository.getAvailableTools(null)).thenReturn(this.mockToolsMap.values());
        Collection<Tool> actual = this.toolInventoryServiceUnderTest.getAvailableTools(null);
        Collection<Tool> expected = this.mockToolsMap.values();
        assertNotNull(actual);
        assertEquals (expected, actual);
    }

    @Test
    public void testGetToolByCode () throws Exception {
        when(this.toolInventoryRepository.getToolByCode(ToolCode.CHNS.name())).thenReturn(this.mockToolsMap.get(ToolCode.CHNS));
        Tool expected = this.mockToolsMap.get(ToolCode.CHNS);
        Tool actual = this.toolInventoryServiceUnderTest.getToolByCode(ToolCode.CHNS.name());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
 }
