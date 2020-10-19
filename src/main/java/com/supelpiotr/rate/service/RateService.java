package com.supelpiotr.rate.service;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.rate.data.Rate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class RateService {

    public BigDecimal getRate(AccountType accountType) throws Exception {

        final String uri = String.format("http://api.nbp.pl/api/exchangerates/rates/A/%s?HTTP Accept=JSON",accountType);
        RestTemplate restTemplate = new RestTemplate();
        Rate result = restTemplate.getForObject(uri, Rate.class);
        if (result != null) {
            return BigDecimal.valueOf(result.getMid());
        }
        throw new Exception("Cannot connect to NBP API");
    }

}
