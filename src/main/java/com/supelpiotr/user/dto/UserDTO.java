package com.supelpiotr.user.dto;

import lombok.Data;

@Data
public class UserDTO {

    private boolean enabled;
    private String firstName;
    private String lastName;
    private String pesel;
    private String password;

}