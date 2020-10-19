package com.supelpiotr.rate.controller;

import com.supelpiotr.account.service.AccountService;
import com.supelpiotr.confirmationToken.service.ConfirmationTokenService;
import com.supelpiotr.rate.data.Rate;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.RestHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class RateController {

    private final RestHelper restHelper;

    @RequestMapping(value="api/get/rate/USD", method= RequestMethod.GET)
    Rate plnToUsdTransfer() {

        final String uri = "http://api.nbp.pl/api/exchangerates/rates/A/USD?HTTP Accept=JSON";

        RestTemplate restTemplate = new RestTemplate();
        Rate result = restTemplate.getForObject(uri, Rate.class);

        return result;

    }

}
