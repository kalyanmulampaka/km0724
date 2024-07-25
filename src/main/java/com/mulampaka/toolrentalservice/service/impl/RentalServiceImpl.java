package com.mulampaka.toolrentalservice.service.impl;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;
import com.mulampaka.toolrentalservice.service.RentalPricingService;
import com.mulampaka.toolrentalservice.service.RentalService;
import com.mulampaka.toolrentalservice.service.ToolInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class RentalServiceImpl implements RentalService {

    @Autowired
    private ToolInventoryService toolInventoryService;

    @Autowired
    private RentalPricingService pricingService;

    /**
     * Return the available tools
     * @param searchRequest SearchRequest for pagination of results
     * @return Collection<Tool>
     */
    @Override
    public Collection<Tool> getAvailableTools(SearchRequest searchRequest) {
        Collection<Tool> tools = this.toolInventoryService.getAvailableTools (searchRequest);
        log.debug("Available tools:{}", tools);
        return tools;
    }

    /** Checksout the specified cart and generates a RentalAgreement
     * Calculate the rental charge for the chosen tools and checkout date
     * @param cart Cart
     * @throws ToolRentalException for all validation errors
     */
    @Override
    public RentalAgreement checkout(Cart cart) {
        Collection<Tool> tools = this.toolInventoryService.getToolsByCodes (cart.getToolCodes());
        if (tools.isEmpty()) {
            throw new ToolRentalException("Tools identifiers cannot be empty.");
        } else {
            log.debug("Calculating rental charge for cart:{}", cart);
            Rental rental = this.pricingService.calculateRentalCharge(cart, tools);
            log.debug("Generating rental agreement for cart:{}", cart);
            RentalAgreement rentalAgreement = this.generateRentalAgreement(rental);
            log.debug("Generated Rental Agreement:{}", rentalAgreement);
            return rentalAgreement;
        }
    }

    /**
     * Generates a rental agreement with the data from the specified cart
     * @param rental Rental
     * @return RentalAgreement
     */
    private RentalAgreement generateRentalAgreement(Rental rental) {
        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .id(1)
                .rental(rental)
                .build();
        rentalAgreement.print();
        return rentalAgreement;
    }
}
