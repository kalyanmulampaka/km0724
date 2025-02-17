package com.mulampaka.toolrentalservice.service.impl;

import com.mulampaka.toolrentalservice.domain.SearchRequest;
import com.mulampaka.toolrentalservice.domain.Tool;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;
import com.mulampaka.toolrentalservice.repository.ToolInventoryRepository;
import com.mulampaka.toolrentalservice.service.ToolInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ToolInventoryServiceImpl implements ToolInventoryService {
    @Autowired
    private ToolInventoryRepository toolInventoryRepository;



    /**
     * Returns the available tools with the specified search parameters
     * @param searchRequest SearchRequest for pagination of results
     * @return Collection<Tool>
     */
    @Override
    public Collection<Tool> getAvailableTools(SearchRequest searchRequest) {
        return this.toolInventoryRepository.getAvailableTools(searchRequest);
    }

    /**
     * Returns the tool with the specified tool code
     * @param toolCode String
     * @return Tool
     * @throws ToolRentalException for validation errors
     */
    @Override
    public Tool getToolByCode(String toolCode) {
        return this.toolInventoryRepository.getToolByCode(toolCode);
    }



}
