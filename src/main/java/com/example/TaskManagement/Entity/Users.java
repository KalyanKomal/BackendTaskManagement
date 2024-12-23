package com.example.TaskManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CascadeType;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    private String id;
    @Column(nullable = false)
    private String username;
    private String password;
    @Column(nullable = false)
    private String role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false,unique = true)
    private String email;



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

//    public List<Tasks> getAsignedtasks() {
//        return assignedTasks;
//    }
//
//    public void setAsignedtasks(List<Tasks> assignedTasks) {
//        this.assignedTasks = assignedTasks;
//    }

//    public List<Notifications> getNotificationsList() {
//        return notificationsList;
//    }
//
//    public void setNotificationsList(List<Notifications> notificationsList) {
//        this.notificationsList = notificationsList;
//    }

    public List<Tasks> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(List<Tasks> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    @ManyToMany(mappedBy = "assignedTo")
    private List<Tasks> assignedTasks;
//    private List<Notifications>notificationsList;y
   @PreRemove
    private void preRemove() {
        if (assignedTasks != null) {
            assignedTasks.forEach(task -> task.getAssignedTo().remove(this));
        }
    }
}