package com.supelpiotr.users.repository;

import com.supelpiotr.users.data.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByPesel(String pesel);
}
