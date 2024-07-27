package com.mulampaka.toolrentalservice.service.impl;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;
import com.mulampaka.toolrentalservice.service.RentalPricingService;
import com.mulampaka.toolrentalservice.service.RentalService;
import com.mulampaka.toolrentalservice.service.ToolInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class RentalServiceImpl implements RentalService {

    @Autowired
    private ToolInventoryService toolInventoryService;

    @Autowired
    private RentalPricingService pricingService;

    /**
     * Return the available tools
     *
     * @param searchRequest SearchRequest for pagination of results
     * @return Collection<Tool>
     */
    @Override
    public Collection<Tool> getAvailableTools(SearchRequest searchRequest) {
        Collection<Tool> tools = this.toolInventoryService.getAvailableTools(searchRequest);
        log.debug("Available tools:{}", tools);
        return tools;
    }

    /**
     * Calculate the rental charge for the chosen tools and checkout date.
     * Generates a RentalAgreement for each tool
     *
     * @param cart Cart
     * @throws ToolRentalException for all validation errors
     */
    @Override
    public List<RentalAgreement> checkout(Cart cart) {
        if (cart.getCartItems().isEmpty()) {
            throw new ToolRentalException("Cart cannot be empty.");
        }
        List<RentalAgreement> rentalAgreements = new ArrayList<>();
        RentalAgreement rentalAgreement = null;
        for (CartItem cartItem : cart.getCartItems()) {
            Tool tool = this.toolInventoryService.getToolByCode(cartItem.getToolCode());
            log.debug("Calculating rental charge for cart item:{}", cartItem);
            RentalItem rentalItem = this.pricingService.calculateRentalCharge(cartItem, tool);
            log.debug("Generating rental agreement for cart:{}", cart);
            rentalAgreement = this.generateRentalAgreement(rentalItem);
            rentalAgreements.add(rentalAgreement);
            log.debug("Generated Rental Agreement:{}", rentalAgreement);

        }
        return rentalAgreements;
    }


    /**
     * Generates a rental agreement with the data from the specified cart
     * @param rentalItem RentalItem
     * @return RentalAgreement
     */
    private RentalAgreement generateRentalAgreement(RentalItem rentalItem) {
        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .rentalItem(rentalItem)
                .build();
        rentalAgreement.nextId();
        if (log.isDebugEnabled()) {
            rentalAgreement.print();
        }
        return rentalAgreement;
    }
}
