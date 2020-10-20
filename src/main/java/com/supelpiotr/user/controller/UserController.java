package com.supelpiotr.user.controller;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.account.dto.AccountPlnDTO;
import com.supelpiotr.account.service.AccountService;
import com.supelpiotr.confirmationToken.service.ConfirmationTokenService;
import com.supelpiotr.exchange.data.ExchangeDTO;
import com.supelpiotr.exchange.service.Exchange;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final ConfirmationTokenService confirmationTokenService;
    private final Exchange exchange;

    @RequestMapping(value="api/register", method= RequestMethod.POST, consumes="application/json")
    ResponseEntity<String> register(@Valid @RequestBody UserDTO userDTO) {

        UserEntity user = userService.mapToEntity(userDTO);
        accountService.prepareDefaultAccount(user);

        try {
            userService.registerUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Registration ok!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="api/create/subaccount/{currency}", method= RequestMethod.GET)
    ResponseEntity<String> createSubAccount(@PathVariable("currency") AccountType accountType) {

        Authentication authentication = getAuthentication();
        UserEntity user = (UserEntity) userService.loadUserByUsername(authentication.getName());
        return accountService.createSubAccount(user, accountType);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="api/user/details", method= RequestMethod.GET)
    UserEntity getUserFromSession() {

        Authentication authentication = getAuthentication();
        return (UserEntity) userService.loadUserByUsername(authentication.getName());

    }

    private Authentication getAuthentication() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx.getAuthentication();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="api/exchange/", method= RequestMethod.POST)
    ResponseEntity<String> exchange(@RequestBody ExchangeDTO exchangeDTO) {

        Authentication authentication = getAuthentication();
        UserEntity user = (UserEntity) userService.loadUserByUsername(authentication.getName());
        try {
            exchange.exchange(exchangeDTO, user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        userService.save(user);

        return new ResponseEntity<>(String.format("%s exchanged to %s",
                exchangeDTO.getInitialCurrency(),
                exchangeDTO.getFinalCurrency()),
                HttpStatus.OK);
    }

}
