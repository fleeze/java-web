package com.zz.repository;

import com.zz.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
    Iterable<Payment> findByAccountId(int payUserId);

}