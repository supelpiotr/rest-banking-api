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

@Service
@RequiredArgsConstructor
public class ForeignExchangeImpl implements Exchange {

    private final UserService userService;
    private final RateService rateService;

    @Override
    public void exchange(ExchangeDTO exchangeDTO, UserEntity userEntity) throws Exception {

        AccountType initialCurrency = exchangeDTO.getInitialCurrency();

        BigDecimal currencyRate = BigDecimal.ONE;

        try {
            currencyRate = rateService.getRate(initialCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BigDecimal requestedValue = exchangeDTO.getRequestedValue();
        BigDecimal initialBalance = userService.getCurrencyBalance(userEntity, initialCurrency);

        if (!initialCurrency.equals(AccountType.PLN)) {

            if (initialBalance.compareTo(requestedValue) < 0) {
                BaseAccount userPlnAccount = userService.getUserPlnAccount(userEntity);
                BigDecimal userPln = userService.getUserPln(userEntity);
                userPlnAccount.setBalance(requestedValue.multiply(currencyRate).add(userPln));
            } else {
                throw new Exception(String
                        .format("Requested value is greater than %s balance", initialCurrency));
            }
        }
//        } else {
//
//            if (initialBalance.compareTo(requestedValue) < 0){
//                BaseAccount userPlnAccount = userService.getUserSubAccount(userEntity,);
//                BigDecimal userPln = userService.getUserPln(userEntity);
//                userPlnAccount.setBalance(requestedValue.multiply(currencyRate).add(userPln));
//            } else {
//                throw new Exception(String
//                        .format("Requested value is greater than %s balance",initialCurrency));
//            }
//        }



    }

}
