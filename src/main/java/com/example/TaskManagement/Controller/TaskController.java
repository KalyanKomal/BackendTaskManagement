package com.example.TaskManagement.Controller;

import com.example.TaskManagement.Dto.TaskDisplayDto;
import com.example.TaskManagement.Dto.TaskDto;
import com.example.TaskManagement.Dto.TaskUpdateDto;
import com.example.TaskManagement.Entity.Tasks;
import com.example.TaskManagement.Entity.Users;
import com.example.TaskManagement.Repository.TaskRepository;
import com.example.TaskManagement.Service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private static final Logger taskcontrollerlogger=LoggerFactory.getLogger(TaskController.class);


    @Autowired
    private TaskService taskService;
    @GetMapping("/getalltasks")
    public List<TaskDisplayDto> getalltasks(){
taskcontrollerlogger.info("Getting all task details");
        return taskService.getAllTasks();
    }
    @PostMapping("/createtask")
    public TaskDisplayDto createtask(@RequestBody TaskDisplayDto taskDto){
        taskcontrollerlogger.info("Creating new task"+taskDto);
        return taskService.createtask(taskDto);

    }

    @GetMapping("/gettaskbyname/{taskname}")
    public ResponseEntity<Map> findtaskbyname(@PathVariable String taskname){
        taskcontrollerlogger.info("Controller layer .Next going into service layer");
       String result= taskService.findtaskbyname(taskname);
        if (result!=null) {
            taskcontrollerlogger.info("Returning task id",result);
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            taskcontrollerlogger.warn("Some error",result);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Task is not present"));
        }
    }



    @GetMapping("/gettaskbyid/{id}")
    public Tasks gettaskbyid(@PathVariable String id){
        taskcontrollerlogger.info("Getting details of task with id"+id);
        return taskService.getbyid(id);
    }

    @GetMapping("/getalltaskbyuserid/{id}")
    public List<TaskDto> getalltaskbyuserid(@PathVariable String id){
        taskcontrollerlogger.info("Getting details of task with id"+id);
        return taskService.getalltasksbyuserid(id);
    }

    @DeleteMapping("/deletetaskbyid/{id}")
    public void deletetaskbyid(@PathVariable String id){
        taskcontrollerlogger.info("Deleting task with id"+id);
        taskService.deletetask(id);
    }
    @PutMapping("/updatetaskbyuser")
    public ResponseEntity<Map> updatetaskbyuser(@RequestBody TaskUpdateDto taskupdateDto){
        taskcontrollerlogger.info("Updating task with id"+taskupdateDto.getId());
       String result= taskService.updatetask(taskupdateDto);
        if (result.equals("Success")) {
            taskcontrollerlogger.info("Success");
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            taskcontrollerlogger.warn("Error",result);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", result));
        }
    }
}
