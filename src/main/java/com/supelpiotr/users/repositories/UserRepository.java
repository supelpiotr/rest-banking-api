package com.supelpiotr.users.repositories;

import com.supelpiotr.users.data.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByPesel(String pesel);
    UserEntity findByUserId(String userId);
}
