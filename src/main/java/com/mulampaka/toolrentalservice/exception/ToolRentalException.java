package com.mulampaka.toolrentalservice.exception;

/**
 * ToolRentalException is unchecked runtime exception used to throw validation errors to the caller.
 */
public class ToolRentalException extends RuntimeException {

    public ToolRentalException(String s) {
        super (s);
    }
}
