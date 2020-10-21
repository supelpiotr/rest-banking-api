package com.supelpiotr.exchange.service;

import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.utils.exceptions.ExchangeException;

public interface Exchange {

    void exchange(ExchangeDTO exchangeDTO,
                  UserEntity userEntity) throws ExchangeException;
}
