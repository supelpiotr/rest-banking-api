package com.supelpiotr.exchange.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.rate.service.RateService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ForeignExchangeImpl implements Exchange {

    private final UserService userService;
    private final RateService rateService;

    @Override
    public void exchange(ExchangeDTO exchangeDTO, UserEntity userEntity) throws Exception {

        AccountType initialCurrency = exchangeDTO.getInitialCurrency();
        AccountType finalCurrency = exchangeDTO.getFinalCurrency();

        BigDecimal currencyRate = BigDecimal.ONE;
        BigDecimal requestedValue = exchangeDTO.getRequestedValue();
        BigDecimal userPlnBalance = userService.getUserPln(userEntity);

        if (!initialCurrency.equals(AccountType.PLN)
                && userService.subAccountActive(userEntity, initialCurrency)) {

            BaseAccount foreignCurrencyAccount = userService.getUserSubAccount(userEntity, initialCurrency);
            BigDecimal foreignCurrencyBalance = userService.getCurrencyBalance(userEntity, initialCurrency);

            try {
                currencyRate = rateService.getRate(initialCurrency);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (foreignCurrencyBalance.compareTo(requestedValue) >= 0) {
                BaseAccount userPlnAccount = userService.getUserPlnAccount(userEntity);

                userPlnAccount.setBalance(requestedValue.multiply(currencyRate).add(userPlnBalance));
                foreignCurrencyAccount.setBalance(foreignCurrencyBalance.subtract(requestedValue));

            } else {
                throw new Exception(String
                        .format("Requested value is greater than %s balance", initialCurrency));
            }

        } else if (initialCurrency.equals(AccountType.PLN)
                && userService.subAccountActive(userEntity, finalCurrency)){


            try {
                currencyRate = rateService.getRate(finalCurrency);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (userPlnBalance.compareTo(requestedValue) > 0){
                BaseAccount userPlnAccount = userService.getUserPlnAccount(userEntity);
                BaseAccount foreignCurrencyAccount = userService.getUserSubAccount(userEntity, finalCurrency);
                BigDecimal foreignCurrencyBalance = userService.getCurrencyBalance(userEntity, finalCurrency);
                foreignCurrencyAccount.setBalance(requestedValue
                        .divide(currencyRate, 2, RoundingMode.CEILING)
                        .add(foreignCurrencyBalance));
                userPlnAccount.setBalance(userPlnBalance.subtract(requestedValue));
            } else {
                throw new Exception(String
                        .format("Requested value is greater than %s balance",initialCurrency));
            }
        } else {
            throw new Exception(String
                    .format("Please create %s sub account",
                            initialCurrency.equals(AccountType.PLN) ? finalCurrency : initialCurrency));
        }


    }

}
