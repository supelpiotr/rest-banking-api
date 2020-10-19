package com.supelpiotr.account.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.account.dto.AccountPlnDTO;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        account.setBalance(userEntity.getInitialPlnBalance());
        accounts.add(account);
        userEntity.setUserAccount(accounts);
        return userEntity;

    }

    public ResponseEntity<String> createSubAccount(UserEntity user, AccountType accountType) {


        if (user != null){
            List<BaseAccount> accounts = user.getUserAccount();
            List<BaseAccount> usdAccounts = user.getUserAccount()
                    .stream()
                    .filter(i -> i.getType().equals(AccountType.USD))
                    .collect(Collectors.toList());
            if (usdAccounts.isEmpty()) {
                BaseAccount account = new BaseAccount();
                account.setType(accountType);
                accounts.add(account);
                user.setUserAccount(accounts);
                user.setSubAccountActive(true);
                userService.save(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("USD sub account already created!");
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Sub account created!");

    }

}
