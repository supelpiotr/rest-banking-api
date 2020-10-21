package com.supelpiotr.user.repository;

import com.supelpiotr.user.data.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByPesel(String pesel);
    List<UserEntity> findAll();
}
