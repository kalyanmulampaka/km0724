package com.mulampaka.toolrentalservice.repository.impl;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;
import com.mulampaka.toolrentalservice.repository.ToolInventoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class ToolInventoryRepositoryImpl implements ToolInventoryRepository {


    private final Map<ToolCode, Tool> toolsMap = new HashMap<>();

    @PostConstruct
    public void init () {
        //load the sample data
        this.getAvailableTools(null);
    }


    /**
     * Returns a list of available tools
     *
     * @return Collection<Tool>
     */
    @Override
    public Collection<Tool> getAvailableTools(SearchRequest searchRequest) {
        /**
         * Hard coding sample data for assessment.
         */
        Tool chainsaw = Tool.builder().id(1)
                .toolCode(ToolCode.CHNS)
                .toolType(ToolType.CHAINSAW)
                .brand(ToolBrand.STIHL.getName())
                .dailyRentalCharge(new BigDecimal("1.49"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(true)
                .build();
        toolsMap.put(chainsaw.getToolCode(), chainsaw);

        Tool ladder = Tool.builder().id(2)
                .toolCode(ToolCode.LADW)
                .toolType(ToolType.LADDER)
                .brand(ToolBrand.WERNER.getName())
                .dailyRentalCharge(new BigDecimal("1.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(true)
                .isHolidayCharged(false)
                .build();
        toolsMap.put(ladder.getToolCode(), ladder);

        Tool jackhammer1 = Tool.builder().id(3)
                .toolCode(ToolCode.JAKD)
                .toolType(ToolType.JACKHAMMER)
                .brand(ToolBrand.DEWALT.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        toolsMap.put(jackhammer1.getToolCode(), jackhammer1);

        Tool jackhammer2 = Tool.builder().id(4)
                .toolCode(ToolCode.JAKR)
                .toolType(ToolType.JACKHAMMER)
                .brand(ToolBrand.RIDGID.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        toolsMap.put(jackhammer2.getToolCode(), jackhammer2);

        return toolsMap.values();
    }

    @Override
    public Tool getToolByCode(String toolCode) {
        try {
            Tool tool = this.toolsMap.get(ToolCode.valueOf(toolCode.toUpperCase()));
            if (tool == null) {
                throw new ToolRentalException("Invalid Tool Code:" + toolCode);
            }
            return tool;
        } catch (Exception e) {
            throw new ToolRentalException("Invalid Tool Code:" + toolCode);
        }
    }


}
