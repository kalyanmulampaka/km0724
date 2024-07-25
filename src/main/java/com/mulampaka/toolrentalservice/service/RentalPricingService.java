package com.mulampaka.toolrentalservice.service;

import com.mulampaka.toolrentalservice.domain.Cart;
import com.mulampaka.toolrentalservice.domain.Rental;
import com.mulampaka.toolrentalservice.domain.Tool;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;

import java.util.Collection;

/**
 * RentalPricingService provides methods to calculate rental charges for the chosen tools and checkout dates.
 */
public interface RentalPricingService {
    /**
     * Calculates the rental charge for the specified cart and tools
     * @param cart Cart
     * @param tools Collection<Tool>
     * @throws ToolRentalException for validation errors
     */
    Rental calculateRentalCharge(Cart cart, Collection<Tool> tools);
}
