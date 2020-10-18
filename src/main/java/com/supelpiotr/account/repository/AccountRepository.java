package com.supelpiotr.account.repository;

import com.supelpiotr.account.data.BaseAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends CrudRepository<BaseAccount, Long> {

}
