package com.supelpiotr.confirmationToken.service;

import com.supelpiotr.confirmationToken.data.ConfirmationToken;
import com.supelpiotr.confirmationToken.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    void saveConfirmationToken(ConfirmationToken confirmationToken) {

        confirmationTokenRepository.save(confirmationToken);
    }
}
