package com.supelpiotr.users.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String userId;
    private String firstName;
    private String lastName;
    private String pesel;
    private String password;
    private String encryptedPassword;

}