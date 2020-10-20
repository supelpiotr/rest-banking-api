package com.supelpiotr.rate.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.rate.data.RateDTO;
import com.supelpiotr.utils.ExchangeException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class RateService {

    public BigDecimal getRate(AccountType accountType) throws ExchangeException {

        final String uri = String.format("http://api.nbp.pl/api/exchangerates/rates/A/%s?HTTP Accept=JSON",accountType);
        RestTemplate restTemplate = new RestTemplate();
        RateDTO result = restTemplate.getForObject(uri, RateDTO.class);
        if (result != null) {
            return BigDecimal.valueOf(result.getRates().get(0).getMid());
        }
        throw new ExchangeException("Cannot connect to NBP API");
    }

}
