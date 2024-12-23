package com.example.TaskManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateDto {
    private String id;
    private String role;
   // private List<String> tasksid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    public List<String> getTasksid() {
//        return tasksid;
//    }
//
//    public void setTasksid(List<String> tasksid) {
//        this.tasksid = tasksid;
//    }
}
