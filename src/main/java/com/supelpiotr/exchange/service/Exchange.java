package com.supelpiotr.exchange.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.user.data.UserEntity;

public interface Exchange {

    void exchange(ExchangeDTO exchangeDTO,
                  UserEntity userEntity) throws Exception;
}
