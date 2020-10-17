package com.supelpiotr.users.service;

import com.supelpiotr.users.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public UserDTO getUserByPesel(String pesel);
}
