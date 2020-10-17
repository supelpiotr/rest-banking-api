package com.supelpiotr.confirmationToken.repository;


import com.supelpiotr.confirmationToken.data.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

}
