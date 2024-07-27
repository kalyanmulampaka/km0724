package com.mulampaka.toolrentalservice.repository;

import com.mulampaka.toolrentalservice.domain.SearchRequest;
import com.mulampaka.toolrentalservice.domain.Tool;

import java.util.Collection;
import java.util.List;

/**
 * Tool Inventory Repository which provides CRUD operations of tool inventory data
 */
public interface ToolInventoryRepository {
    Collection<Tool> getAvailableTools(SearchRequest searchRequest);

    Tool getToolByCode(String toolCode);
}
