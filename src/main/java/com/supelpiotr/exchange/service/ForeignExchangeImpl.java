package com.supelpiotr.exchange.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.rate.service.RateService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.ExchangeException;
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
    public void exchange(ExchangeDTO exchangeDTO, UserEntity userEntity) throws ExchangeException {

        AccountType initialCurrency = exchangeDTO.getInitialCurrency();
        AccountType finalCurrency = exchangeDTO.getFinalCurrency();
        BigDecimal requestedValue = exchangeDTO.getRequestedValue();

        if (!initialCurrency.equals(AccountType.PLN)
                && userService.subAccountActive(userEntity, initialCurrency)) {

            foreignToPlnExchange(userEntity, initialCurrency, requestedValue);

        } else if (initialCurrency.equals(AccountType.PLN)
                && userService.subAccountActive(userEntity, finalCurrency)){
            
            plnToForeignExchange(userEntity, finalCurrency, requestedValue);

        } else {
            throw new ExchangeException(String
                    .format("Please create %s sub account",
                            initialCurrency.equals(AccountType.PLN) ? finalCurrency : initialCurrency));
        }


    }

    private void plnToForeignExchange(UserEntity userEntity, AccountType finalCurrency, BigDecimal requestedValue) {
        try {

            BigDecimal userPlnBalance = userService.getUserPln(userEntity);
            BigDecimal currencyRate = rateService.getRate(finalCurrency);

            if (userPlnBalance.compareTo(requestedValue) > 0){
                BaseAccount userPlnAccount = userService.getUserPlnAccount(userEntity);
                BaseAccount foreignCurrencyAccount = userService.getUserSubAccount(userEntity, finalCurrency);
                BigDecimal foreignCurrencyBalance = userService.getCurrencyBalance(userEntity, finalCurrency);
                foreignCurrencyAccount.setBalance(requestedValue
                        .divide(currencyRate, 2, RoundingMode.CEILING)
                        .add(foreignCurrencyBalance));
                userPlnAccount.setBalance(userPlnBalance.subtract(requestedValue));
            } else {
                throw new ExchangeException(String
                        .format("Requested value is greater than %s balance",AccountType.PLN));
            }

        } catch (ExchangeException e) {
            e.printStackTrace();
        }
    }

    private void foreignToPlnExchange(UserEntity userEntity, AccountType initialCurrency, BigDecimal requestedValue) {
        BaseAccount foreignCurrencyAccount = userService.getUserSubAccount(userEntity, initialCurrency);
        BigDecimal foreignCurrencyBalance = userService.getCurrencyBalance(userEntity, initialCurrency);

        try {

            BigDecimal userPlnBalance = userService.getUserPln(userEntity);
            BigDecimal currencyRate = rateService.getRate(initialCurrency);

            if (foreignCurrencyBalance.compareTo(requestedValue) >= 0) {
                BaseAccount userPlnAccount = userService.getUserPlnAccount(userEntity);

                userPlnAccount.setBalance(requestedValue.multiply(currencyRate).add(userPlnBalance));
                foreignCurrencyAccount.setBalance(foreignCurrencyBalance.subtract(requestedValue));

            } else {
                throw new ExchangeException(String
                        .format("Requested value is greater than %s balance", initialCurrency));
            }

        } catch (ExchangeException e) {
            e.printStackTrace();
        }
    }

}
