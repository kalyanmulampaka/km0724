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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
        when(this.toolInventoryRepository.getAvailableTools(null)).thenReturn(this.mockToolsMap.values());
        Collection<Tool> actual = this.toolInventoryServiceUnderTest.getAvailableTools(null);
        Collection<Tool> expected = this.mockToolsMap.values();
        assertNotNull(actual);
        assertEquals (expected, actual);
    }

    @Test
    public void testGetToolsByIds () throws Exception {
        when(this.toolInventoryRepository.getToolsByIds(List.of(1,2))).thenReturn(List.of(this.mockToolsMap.get(1), this.mockToolsMap.get(2)));
        Collection<Tool> expected = List.of (this.mockToolsMap.get(1), this.mockToolsMap.get(2));
        Collection<Tool> actual = this.toolInventoryServiceUnderTest.getToolsByIds(List.of(1,2));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
 }
