package com.mulampaka.toolrentalservice.repository;

import com.mulampaka.toolrentalservice.domain.SearchRequest;
import com.mulampaka.toolrentalservice.domain.Tool;

import java.util.Collection;
import java.util.List;

public interface ToolInventoryRepository {
    Collection<Tool> getAvailableTools(SearchRequest searchRequest);

    Collection<Tool> getToolsByIds(List<Integer> toolIds);
}
