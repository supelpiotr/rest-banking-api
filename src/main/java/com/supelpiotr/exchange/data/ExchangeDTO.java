package com.supelpiotr.exchange.data;

import com.supelpiotr.account.data.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExchangeDTO {

    private AccountType initialCurrency;
    private AccountType finalCurrency;
    private BigDecimal requestedValue;

}
