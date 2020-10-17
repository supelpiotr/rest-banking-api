package com.supelpiotr.users.repositories;

import com.supelpiotr.users.data.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
    User findByPesel(String pesel);
    User findByUserId(String userId);
}
