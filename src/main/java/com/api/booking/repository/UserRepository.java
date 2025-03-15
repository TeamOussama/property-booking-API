package com.api.booking.repository;

import com.api.booking.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

}
