package com.mulampaka.toolrentalservice.domain;

import lombok.Data;


/**
 *  Represents the SearchRequest entity used for pagination
 */
@Data
public class SearchRequest {
    private int size = 20; // default to 20 records
    private int page;
    private int count;
    private String sortBy;
    private String sortDirection = SortDirection.ASC.name(); // default to ascending sort
}
