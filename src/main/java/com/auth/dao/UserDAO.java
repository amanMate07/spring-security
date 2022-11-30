package com.auth.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    
}
