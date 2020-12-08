package com.supelpiotr.exchange.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.exceptions.ExchangeException;

import java.math.BigDecimal;

public interface Exchange {

    default void exchange(ExchangeDTO exchangeDTO, UserEntity userEntity) throws ExchangeException{
        AccountType initialCurrency = exchangeDTO.getInitialCurrency();
        AccountType finalCurrency = exchangeDTO.getFinalCurrency();
        BigDecimal requestedValue = exchangeDTO.getRequestedValue();
        AccountType accountType = initialCurrency.equals(AccountType.PLN) ? finalCurrency : initialCurrency;

        if (UserService.subAccountActive(userEntity, initialCurrency)) {

            exchange(userEntity, accountType, requestedValue);

        } else {
            throw new ExchangeException(String
                    .format("Please create %s sub account",
                            accountType));
        }

    }

    void exchange(UserEntity userEntity, AccountType currency, BigDecimal requestedValue) throws ExchangeException;

}
