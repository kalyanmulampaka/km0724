package com.mulampaka.toolrentalservice.domain;

import lombok.Getter;

/**
 * Enumeration of Tool brand names
 */
@Getter
public enum ToolBrand {
    STIHL ("Stihl"),
    WERNER ("Werner"),
    DEWALT ("DeWalt"),
    RIDGID ("Ridgid")
    ;

    private final String name;

    private ToolBrand (String name) {
        this.name = name;
    }

}
