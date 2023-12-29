package com.zz.repository;

import com.zz.model.User;
import org.springframework.data.repository.CrudRepository;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByUserName(String userName);

    boolean existsByUserNameAndPassword(String userName, String password);

    User findByUserName(String userName);

}
