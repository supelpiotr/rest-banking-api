package com.supelpiotr.account.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.account.dto.AccountPlnDTO;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.utils.exceptions.SubAccountCreationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

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

    public void createSubAccount(UserEntity user, AccountType accountType) throws SubAccountCreationException {


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
            } else {
                throw new SubAccountCreationException (String.format("%s sub account already created!",accountType));
            }
        }

    }

}
