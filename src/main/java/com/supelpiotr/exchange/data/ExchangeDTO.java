package com.supelpiotr.exchange.data;

import com.supelpiotr.account.data.AccountType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@Builder
public class ExchangeDTO {

    private AccountType initialCurrency;
    private AccountType finalCurrency;
    @DecimalMin(value = "0.01")
    private BigDecimal requestedValue;

}
