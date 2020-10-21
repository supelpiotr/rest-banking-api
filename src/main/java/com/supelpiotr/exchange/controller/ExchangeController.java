package com.supelpiotr.exchange.controller;

import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.exchange.service.Exchange;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class ExchangeController {

    private final UserService userService;
    private final Exchange exchange;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value="api/exchange")
    public ResponseEntity<String> exchange(@RequestBody ExchangeDTO exchangeDTO) {

        Authentication authentication = getAuthentication();
        UserEntity user = (UserEntity) userService.loadUserByUsername(authentication.getName());
        try {
            exchange.exchange(exchangeDTO, user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        userService.save(user);

        return new ResponseEntity<>(String.format("%s exchanged to %s",
                exchangeDTO.getInitialCurrency(),
                exchangeDTO.getFinalCurrency()),
                HttpStatus.OK);
    }

    private Authentication getAuthentication() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx.getAuthentication();
    }

}
