package com.supelpiotr.exchange.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.rate.service.RateService;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.exceptions.ExchangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeFactory {

    private final UserService userService;
    private final RateService rateService;

    public Exchange createExchange(ExchangeDTO exchangeDTO) throws ExchangeException {

        if (exchangeDTO.getInitialCurrency().equals(AccountType.PLN)){
            return new ExchangePln(userService, rateService);
        } else {
            return new ExchangeForeign(userService, rateService);
        }

    }

}
