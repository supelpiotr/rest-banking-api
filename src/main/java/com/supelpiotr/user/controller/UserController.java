package com.supelpiotr.user.controller;

import com.supelpiotr.confirmationToken.service.ConfirmationTokenService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.service.UserService;
import com.supelpiotr.utils.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ConfirmationTokenService confirmationTokenService;

    @RequestMapping(value="/register", method= RequestMethod.POST, consumes="application/json")
    ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {

        UserEntity user = userService.mapToEntity(userDTO);
        userService.registerUser(user);

        return new ResponseEntity<>("Registration ok!", HttpStatus.OK);
    }

}
