package com.supelpiotr.user.service;

import com.supelpiotr.confirmationToken.data.ConfirmationToken;
import com.supelpiotr.confirmationToken.service.ConfirmationTokenService;
import com.supelpiotr.user.data.UserEntity;
import com.supelpiotr.user.dto.UserDTO;
import com.supelpiotr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String pesel) throws UsernameNotFoundException {

        final Optional<UserEntity> optionalUser = userRepository.findByPesel(pesel);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        else {
            throw new UsernameNotFoundException(MessageFormat.format("User with pesel {0} not found.", pesel));
        }
    }

    public void registerUser(UserEntity user) {

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        final UserEntity createdUser = userRepository.save(user);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

    }

    public UserEntity mapToEntity(UserDTO userDTO) {

        userDTO.setEnabled(true);
        return new ModelMapper().map(userDTO, UserEntity.class);

    }

}


