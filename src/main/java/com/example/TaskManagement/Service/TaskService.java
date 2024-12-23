package com.example.TaskManagement.Service;

import com.example.TaskManagement.Dto.TaskDisplayDto;
import com.example.TaskManagement.Dto.TaskDto;
import com.example.TaskManagement.Dto.TaskUpdateDto;
import com.example.TaskManagement.Entity.Tasks;
import com.example.TaskManagement.Entity.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    public List<TaskDisplayDto> getAllTasks();
    public Tasks getbyid(String id);
    public TaskDisplayDto createtask(TaskDisplayDto taskDto);
    public void deletetask(String id);
    public String updatetask(TaskUpdateDto taskupdateDto);

    public String findtaskbyname(String taskname);

   public List<TaskDto> getalltasksbyuserid(String id);
}
