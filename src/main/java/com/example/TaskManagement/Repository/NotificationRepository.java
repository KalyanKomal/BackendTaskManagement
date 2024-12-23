package com.example.TaskManagement.Repository;

import com.example.TaskManagement.Entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications,String> {
}
