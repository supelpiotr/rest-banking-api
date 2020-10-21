package com.supelpiotr.utils;

import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserService userService;

    public void run(ApplicationArguments args) {
        userService.saveOnSturtup(new UserEntity
                (
                        "Testomir",
                        "Testowy",
                        "87061612345",
                        "test123",
                        BigDecimal.valueOf(200)
                ));
    }
}
