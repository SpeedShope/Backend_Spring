package com.pi.Centrale_Achat.repositories;

import com.pi.Centrale_Achat.entities.Bill;
import com.pi.Centrale_Achat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findByVerificationCode(String code);

    List<User> findAll();

}