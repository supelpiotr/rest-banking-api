package com.supelpiotr.account.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.account.dto.AccountPlnDTO;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;

    public BaseAccount mapToEntity(AccountPlnDTO accountDTO) {

        return new ModelMapper().map(accountDTO, BaseAccount.class);

    }

    public UserEntity prepareDefaultAccount(UserEntity userEntity){

        List<BaseAccount> accounts = new ArrayList<>();
        BaseAccount account = new BaseAccount();
        account.setType(AccountType.PLN);
        account.setBalancePLN(userEntity.getInitialPlnBalance());
        accounts.add(account);
        userEntity.setUserAccount(accounts);
        return userEntity;

    }

    public ResponseEntity<String> createSubaccount(UserEntity user) {


        if (user != null){
            List<BaseAccount> accounts = user.getUserAccount();
            List<BaseAccount> eurAccounts = user.getUserAccount()
                    .stream()
                    .filter(i -> i.getType().equals(AccountType.EUR))
                    .collect(Collectors.toList());
            if (eurAccounts.isEmpty()) {
                BaseAccount account = new BaseAccount();
                account.setType(AccountType.EUR);
                accounts.add(account);
                user.setUserAccount(accounts);
                userService.save(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("EUR subaccount already created!");
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Subaccount created!");

    }

}
