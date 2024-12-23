package com.example.TaskManagement.Dto;

import com.example.TaskManagement.Entity.Notifications;
import com.example.TaskManagement.Entity.Tasks;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String password;
    private String role;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//    private List<String> asignedtasksids;
   // private List<String> notificationsListids;

//    public List<String> getNotificationsList() {
//        return notificationsList;
//    }
//
//    public void setNotificationsList(List<String> notificationsList) {
//        this.notificationsList = notificationsList;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public List<String> getAsignedtasks() {
//        return asignedtasksids;
//    }
//
//    public void setAsignedtasks(List<String> asignedtasks) {
//        this.asignedtasksids = asignedtasks;
//    }
}
