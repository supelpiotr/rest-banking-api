package com.supelpiotr.user.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserDTO {

    private boolean enabled;
    private String firstName;
    private String lastName;
    private String pesel;
    private String password;
    private BigDecimal initialPlnBalance;

}