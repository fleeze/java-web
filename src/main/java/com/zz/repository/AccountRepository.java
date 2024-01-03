package com.zz.repository;

import com.zz.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Iterable<Account> findAllByUserId(int userId);

    void deleteByUserIdAndId(int userId, int accountId);

    Account findByUserIdAndId(int userId, int accountId);
}