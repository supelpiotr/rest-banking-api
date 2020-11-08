package com.supelpiotr.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supelpiotr.account.data.BaseAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private boolean enabled;

    private String firstName;

    private String lastName;

    private String pesel;

    private String password;

    private BigDecimal initialPlnBalance;

    private List<BaseAccount> userAccount;

}