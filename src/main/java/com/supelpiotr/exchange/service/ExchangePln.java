package com.supelpiotr.exchange.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.rate.service.RateService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.exceptions.ExchangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExchangePln implements Exchange {

    private final UserService userService;
    private final RateService rateService;

    @Override
    public void exchange(UserEntity userEntity, AccountType currency, BigDecimal requestedValue) throws ExchangeException {

        BigDecimal userPlnBalance = userService.getUserPln(userEntity);
        BigDecimal currencyRate = rateService.getRate(currency);

        if (userPlnBalance.compareTo(requestedValue) > 0){
            BaseAccount userPlnAccount = userService.getUserPlnAccount(userEntity);
            BaseAccount foreignCurrencyAccount = userService.getUserSubAccount(userEntity, currency);
            BigDecimal foreignCurrencyBalance = userService.getCurrencyBalance(userEntity, currency);
            foreignCurrencyAccount.setBalance(requestedValue
                    .divide(currencyRate, 2, RoundingMode.CEILING)
                    .add(foreignCurrencyBalance));
            userPlnAccount.setBalance(userPlnBalance.subtract(requestedValue));
        } else {
            throw new ExchangeException(String
                    .format("Requested value is greater than %s balance",AccountType.PLN));
        }

    }

}
