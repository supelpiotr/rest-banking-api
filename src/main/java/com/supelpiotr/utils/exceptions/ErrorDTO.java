package com.supelpiotr.utils.exceptions;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final String description;

    public ErrorDTO(String message, String description) {
        this.message = message;
        this.description = description;
    }

}
