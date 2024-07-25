package com.mulampaka.toolrentalservice.service;

import com.mulampaka.toolrentalservice.domain.SearchRequest;
import com.mulampaka.toolrentalservice.domain.Tool;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;

import java.util.Collection;
import java.util.List;

/**
 * ToolInventoryService provides methods to manage tool inventory.
 */
public interface ToolInventoryService {
    /**
     * Returns the available tools from inventory
     * @param searchRequest SearchRequest for pagination of results
     * @return Collection<Tool> - list of available tools
     */
    Collection<Tool> getAvailableTools(SearchRequest searchRequest);

    /**
     * Returns the Tool objects for the specified tool identifiers
     * @param toolIds List<Integer>
     * @return Collection<Tool>
     * @throws ToolRentalException for validation errors
     */
    Collection<Tool> getToolsByIds(List<Integer> toolIds);
}
