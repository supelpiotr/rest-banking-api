package com.supelpiotr.user.controller;

import com.supelpiotr.account.service.AccountService;
import com.supelpiotr.exchange.service.Exchange;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final Exchange exchange;

    @PostMapping(value="api/register", consumes="application/json")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO userDTO) {

        try {
            userService.registerUser(userDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Registration ok!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value="api/user/details")
    public UserEntity getUserFromSession() {

        Authentication authentication = getAuthentication();
        return (UserEntity) userService.loadUserByUsername(authentication.getName());

    }

    @GetMapping(value="api/users")
    public List<UserEntity> getUsers() {

        return userService.findAllUsers();

    }

    private Authentication getAuthentication() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx.getAuthentication();
    }

}

