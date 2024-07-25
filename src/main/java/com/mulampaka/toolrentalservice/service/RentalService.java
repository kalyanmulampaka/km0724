package com.mulampaka.toolrentalservice.service;

import com.mulampaka.toolrentalservice.domain.Cart;
import com.mulampaka.toolrentalservice.domain.RentalAgreement;
import com.mulampaka.toolrentalservice.domain.SearchRequest;
import com.mulampaka.toolrentalservice.domain.Tool;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;

import java.util.Collection;

/**
 * RentalService is the main service which provides methods to get tools, checkout cart and generated rental agreements.
 */
public interface RentalService {
    /**
     * Returns the available tools from inventory
     * @param searchRequest SearchRequest for pagination of results
     * @return Collection<Tool> - list of available tools
     */
    Collection<Tool> getAvailableTools(SearchRequest searchRequest);

    /**
     * Calculates the rental charge and generates a rental agreement for the specified cart
     * @param cart Cart
     * @return RentalAgreement
     * @throws ToolRentalException for validation errors
     */
    RentalAgreement checkout (Cart cart);

}
