package com.supelpiotr.user.controller;

import com.supelpiotr.account.data.BaseAccount;
import com.supelpiotr.account.dto.AccountPlnDTO;
import com.supelpiotr.account.service.AccountService;
import com.supelpiotr.confirmationToken.service.ConfirmationTokenService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final ConfirmationTokenService confirmationTokenService;

    @RequestMapping(value="api/register", method= RequestMethod.POST, consumes="application/json")
    ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO) {

        UserEntity user = userService.mapToEntity(userDTO);
        accountService.prepareDefaultAccount(user);

        userService.registerUser(user);

        return new ResponseEntity<>("Registration ok!", HttpStatus.OK);
    }

    @RequestMapping(value="api/create/subaccount", method= RequestMethod.GET)
    ResponseEntity<String> createSubaccount() {

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();
        if (authentication != null) {
            UserEntity user = (UserEntity) userService.loadUserByUsername(authentication.getName());
            return accountService.createSubaccount(user);
        }

        return new ResponseEntity<>("Please login", HttpStatus.OK);
    }

}
