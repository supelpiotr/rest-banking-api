package com.supelpiotr.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ResponseDTO implements Serializable {

    private boolean success = true;

    @JsonProperty("isSuccess")
    public boolean isSuccess() {
        return success;
    }

}
