package com.example.TaskManagement.Repository;

import com.example.TaskManagement.Entity.Tasks;
import com.example.TaskManagement.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Tasks,String> {
    @Query("SELECT t FROM Tasks t WHERE t.title = :title")
    Optional<Tasks> findByTitle(@Param("title")String tasktitle);
}
