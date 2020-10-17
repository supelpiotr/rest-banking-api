package com.supelpiotr.users.service;

import com.supelpiotr.users.data.UserEntity;
import com.supelpiotr.users.dto.UserDTO;
import com.supelpiotr.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@RequiredArgsConstructor
public class UsersServiceImpl implements UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    @Override
    public UserDTO getUserByPesel(String pesel) {
        UserEntity userEntity = userRepository.findByPesel(pesel);

        if (userEntity == null) {
            throw new UsernameNotFoundException(pesel);
        }

        return new ModelMapper().map(userEntity, UserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String pesel) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByPesel(pesel);

        if (userEntity == null) {
            throw new UsernameNotFoundException(pesel);
        }
        return new User(userEntity.getPesel(), userEntity.getEncryptedPassword(),
                true,
                true, true,
                true, new ArrayList<>());
    }

}


