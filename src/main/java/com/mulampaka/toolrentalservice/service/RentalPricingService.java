package com.mulampaka.toolrentalservice.service;

import com.mulampaka.toolrentalservice.domain.CartItem;
import com.mulampaka.toolrentalservice.domain.RentalItem;
import com.mulampaka.toolrentalservice.domain.Tool;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;

/**
 * RentalPricingService provides methods to calculate rental charges for the chosen tools and checkout dates.
 */
public interface RentalPricingService {
    /**
     * Calculates the rental charge for the specified cart item and tool
     * @param cartItem CartItem
     * @param tool Tool
     * @throws ToolRentalException for validation errors
     */
    RentalItem calculateRentalCharge(CartItem cartItem, Tool tool);
}
