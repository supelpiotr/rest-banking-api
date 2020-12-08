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

@Service
@RequiredArgsConstructor
public class ExchangeForeign implements Exchange {

    private final UserService userService;
    private final RateService rateService;

    @Override
    public void exchange(UserEntity userEntity, AccountType currency, BigDecimal requestedValue) throws ExchangeException {
        BaseAccount foreignCurrencyAccount = userService.getUserSubAccount(userEntity, currency);
        BigDecimal foreignCurrencyBalance = userService.getCurrencyBalance(userEntity, currency);

            BigDecimal userPlnBalance = userService.getUserPln(userEntity);
            BigDecimal currencyRate = rateService.getRate(currency);

            if (foreignCurrencyBalance.compareTo(requestedValue) >= 0) {
                BaseAccount userPlnAccount = userService.getUserPlnAccount(userEntity);

                userPlnAccount.setBalance(requestedValue.multiply(currencyRate).add(userPlnBalance));
                foreignCurrencyAccount.setBalance(foreignCurrencyBalance.subtract(requestedValue));

            } else {
                throw new ExchangeException(String
                        .format("Requested value is greater than %s balance", currency));
            }

    }

}
