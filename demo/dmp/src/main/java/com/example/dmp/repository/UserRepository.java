package com.example.dmp.repository;

import com.example.dmp.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @version "\$Id$" kenan
 */
public interface UserRepository extends JpaRepository<User, String> {

    public User findUserByBreId(String id);
    public User findUserByDmpId(String id);
}
