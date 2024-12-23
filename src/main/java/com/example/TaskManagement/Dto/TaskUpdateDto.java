package com.example.TaskManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDto {
    private String id;
 //   private String title;
    private String status;
    //private List<String> assignedTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public List<String> getAssignedTo() {
//        return assignedTo;
//    }
//
//    public void setAssignedTo(List<String> assignedTo) {
//        this.assignedTo = assignedTo;
//    }

//    public Timestamp getDueDate() {
//        return dueDate;
//    }
//
//    public void setDueDate(Timestamp dueDate) {
//        this.dueDate = dueDate;
//    }
//
//    private Timestamp dueDate;
}
