package com.supelpiotr.users.service;

import com.supelpiotr.users.data.UserEntity;
import com.supelpiotr.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.MessageFormat;
import java.util.Optional;

@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

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

}


