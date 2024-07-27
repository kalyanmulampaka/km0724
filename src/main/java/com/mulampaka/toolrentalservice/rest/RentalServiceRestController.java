package com.mulampaka.toolrentalservice.rest;

import com.mulampaka.toolrentalservice.domain.*;
import com.mulampaka.toolrentalservice.exception.ToolRentalException;
import com.mulampaka.toolrentalservice.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class RentalServiceRestController {

    @Autowired
    private RentalService rentalService;

    @Operation(summary = "Returns the health status of this service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the health status of this service.",
                    content = { @Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/health")
    public ResponseEntity<String> health () {
        log.debug("Received Ping Request");
        return ResponseEntity.ok("Tool Rental Server is UP.");
    }

    /**
     * Returns the available tools with the specified pagination parameters.
     * @param searchRequest SearchRequest.
     * @return Collection<Tool>
     */
    @Operation(summary = "Returns the available tools")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the available tools for rent.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tool.class)) }),
            @ApiResponse(responseCode = "500", description = "Error occurred while retrieving available tools.",
                    content = { @Content(mediaType = "application/json")})
    })
    @GetMapping ("/tools")
    public ResponseEntity<Object> getAvailableTools (@RequestParam (required = false) SearchRequest searchRequest) {
        try {
            Collection<Tool> tools = this.rentalService.getAvailableTools(searchRequest);
            return ResponseEntity.ok(tools);
        } catch (ToolRentalException tre) {
            return ResponseEntity.badRequest().body(tre.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred while retrieving available tools");
        }
    }

    /**
     * Checks out the specified cart - calculates the rental charge and generates a rental agreement for each tool
     * @param cart Cart
     * @return List<RentalAgreement>
     */
    @Operation(summary = "Checkout the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the rental agreement.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalAgreement.class)) }),
            @ApiResponse(responseCode = "400", description = "One of the Validation errors: Checkout Date cannot be in the past. Invalid Checkout Date. Invalid Discount Percent. Valid Discount Percent Range:0-100. Rental days must be greater than 0. Tools Codes cannot be empty. Invalid Tool Codes.",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error occurred while checkout.",
                    content = { @Content(mediaType = "application/json")})
    })
    @PostMapping("/checkout")
    public ResponseEntity<Object> checkout (@RequestBody Cart cart) {
        log.debug("Request received to checkout for cart:{}", cart);
        try {
            this.validateCart(cart);
            List<RentalAgreement> rentalAgreement = this.rentalService.checkout(cart);
            return ResponseEntity.ok(rentalAgreement);
        } catch (ToolRentalException tre) {
            return ResponseEntity.badRequest().body(tre.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred while checkout:" + e.getMessage());
        }
    }

    /**
     * Validates the specified cart
     * @param cart Cart
     * @throws ToolRentalException for validation errors
     */
    private void validateCart (Cart cart) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getCheckoutDate().isBefore(LocalDate.now())) {
                throw new ToolRentalException("Tool Code:" + cartItem.getToolCode() + ", Checkout Date cannot be in the past. Invalid Checkout Date:" + cartItem.getCheckoutDate());
            }
            Integer discountPercent = cartItem.getDiscountPercent();
            if (discountPercent != null && (discountPercent < 0 || discountPercent > 100)) {
                throw new ToolRentalException("Tool Code:" + cartItem.getToolCode() + ", Invalid Discount Percent:" + discountPercent + ". Valid Discount Percent Range:0-100");
            }
            if (cartItem.getRentalDays() < 1) {
                throw new ToolRentalException("Tool Code:" + cartItem.getToolCode() + ", Rental days must be greater than 0.");
            }
        }
    }
}
