package com.supelpiotr.account.controller;

import com.supelpiotr.account.data.AccountType;
import com.supelpiotr.account.service.AccountService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.exceptions.SubAccountCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value="api/create/subaccount/{currency}")
    public ResponseEntity<String> createSubAccount(@PathVariable("currency") AccountType accountType) {

        Authentication authentication = getAuthentication();
        UserEntity user = (UserEntity) userService.loadUserByUsername(authentication.getName());

        try {
            accountService.createSubAccount(user, accountType);
            userService.save(user);
        } catch (SubAccountCreationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Sub account created!");

    }

    private Authentication getAuthentication() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx.getAuthentication();
    }

}
