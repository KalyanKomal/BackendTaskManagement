package com.example.TaskManagement.Repository;

import com.example.TaskManagement.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,String> {
    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Optional<Users> findByUsername(@Param("username")String username);
    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByemail(@Param("email")String email);
}
