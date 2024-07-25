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
                .tooltype(ToolType.CHAINSAW)
                .brand(ToolBrand.STIHL.getName())
                .dailyRentalCharge(new BigDecimal("1.49"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(true)
                .build();
        toolsMap.put(chainsaw.getToolCode(), chainsaw);

        Tool ladder = Tool.builder().id(2)
                .toolCode(ToolCode.LADW)
                .tooltype(ToolType.LADDER)
                .brand(ToolBrand.WERNER.getName())
                .dailyRentalCharge(new BigDecimal("1.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(true)
                .isHolidayCharged(false)
                .build();
        toolsMap.put(ladder.getToolCode(), ladder);

        Tool jackhammer1 = Tool.builder().id(3)
                .toolCode(ToolCode.JAKD)
                .tooltype(ToolType.JACKHAMMER)
                .brand(ToolBrand.DEWALT.getName())
                .dailyRentalCharge(new BigDecimal("2.99"))
                .isWeekdayCharged(true)
                .isWeekendCharged(false)
                .isHolidayCharged(false)
                .build();
        toolsMap.put(jackhammer1.getToolCode(), jackhammer1);

        Tool jackhammer2 = Tool.builder().id(4)
                .toolCode(ToolCode.JAKR)
                .tooltype(ToolType.JACKHAMMER)
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
    public Collection<Tool> getToolsByCodes(List<String> toolCodes) {
        Collection<Tool> tools = new ArrayList<>();
        List<String> invalidCodes = new ArrayList<>();
        for (String code :toolCodes) {
            ToolCode toolCode = null;
            try {
                toolCode = ToolCode.valueOf(code);
                Tool tool = this.toolsMap.get(toolCode);
                if (tool != null) {
                    tools.add(tool);
                }  else  {
                    invalidCodes.add(code);
                }
            } catch (Exception e) {
                invalidCodes.add(code);
            }
        }
        if (!invalidCodes.isEmpty()) {
            throw new ToolRentalException("Invalid Tool Codes:" + invalidCodes);
        }
        return tools;
    }


}
