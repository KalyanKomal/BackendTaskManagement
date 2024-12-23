package com.example.TaskManagement.ServiceImpl;

import com.example.TaskManagement.Dto.TaskDisplayDto;
import com.example.TaskManagement.Dto.TaskDto;
import com.example.TaskManagement.Dto.TaskUpdateDto;
import com.example.TaskManagement.Entity.Tasks;
import com.example.TaskManagement.Entity.Users;
import com.example.TaskManagement.Repository.TaskRepository;
import com.example.TaskManagement.Repository.UserRepository;
import com.example.TaskManagement.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger taskservicelogger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public List<TaskDisplayDto> getAllTasks() {
        taskservicelogger.info("Fetching all tasks from the repository");
//        List<TaskDto>taskDtos=new ArrayList<>();
        List<Tasks>tasksList=taskRepository.findAll();
        List<TaskDisplayDto> taskDisplayDtoList=new ArrayList<>();
        for(int i=0;i<tasksList.size();i++){
            Tasks T1=tasksList.get(i);
            TaskDisplayDto TD1=new TaskDisplayDto();
            TD1.setId(T1.getId());
            TD1.setStatus(T1.getStatus());
            TD1.setTitle(T1.getTitle());
            TD1.setCreatedBy(T1.getCreatedBy());
            TD1.setDueDate(T1.getDueDate());
            List<Users> L1 = T1.getAssignedTo();
            List<String> L2=new ArrayList<>();
            for(int j=0;j<L1.size();j++){
                L2.add(L1.get(j).getId());
            }
            TD1.setAssignedTo(L2);
            taskDisplayDtoList.add(TD1);
        }
     // List<String> taskids=new ArrayList<>();
//        for(int i=0;i<tasksList.size();i++){
//          TaskDto newtaskdto=new TaskDto();
//        }

        taskservicelogger.info("Successfully fetched {} tasks", taskDisplayDtoList.size());
        return taskDisplayDtoList;
    }

    @Override
    public Tasks getbyid(String id) {
        taskservicelogger.info("Fetching task by ID: {}", id);
        Optional<Tasks> task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskservicelogger.info("Task found with ID: {}", id);
            return task.get();
        } else {
            taskservicelogger.error("Task not found with ID: {}", id);
            throw new RuntimeException("Task not found");
        }

    }

    @Override
    public TaskDisplayDto createtask(TaskDisplayDto taskDto) {
        List<Users> usersList=new ArrayList<>();
        List<String> stringList=new ArrayList<>();
        Tasks newtask = new Tasks();

        taskservicelogger.info("Creating new task with title: {}", taskDto.getTitle());
String tasktitle= taskDto.getTitle();
        Optional<Tasks> alreadyexisttask = taskRepository.findByTitle(tasktitle);
        if(alreadyexisttask.isEmpty()) {
            newtask.setId(UUID.randomUUID().toString());
            newtask.setCreatedBy(taskDto.getCreatedBy());
            newtask.setStatus(taskDto.getStatus());
            newtask.setDueDate(taskDto.getDueDate());
            newtask.setTitle(taskDto.getTitle());
        }else{
            newtask.setId(alreadyexisttask.get().getId());
            if(!alreadyexisttask.get().getCreatedBy().equals("")) {
                newtask.setCreatedBy(alreadyexisttask.get().getCreatedBy());
            }
            if(!alreadyexisttask.get().getStatus().equals("")) {
                newtask.setStatus(alreadyexisttask.get().getStatus());
            }
            if(alreadyexisttask.get().getDueDate()!=null) {
                newtask.setDueDate(alreadyexisttask.get().getDueDate());
            }
            if(!alreadyexisttask.get().getTitle().equals("")) {
                newtask.setTitle(alreadyexisttask.get().getTitle());
            }
        }
            for (int i = 0; i < taskDto.getAssignedTo().size(); i++) {
                Optional<Users> optionaluser = userRepository.findById(taskDto.getAssignedTo().get(i));
                if (!optionaluser.isEmpty()) {
                    Users exist_user = optionaluser.get();
//                List<Tasks> tasksList=exist_user.getAsignedtasks();
//                tasksList.add(newtask);
                    if (!usersList.contains(exist_user)) {
                        usersList.add(exist_user);
                        stringList.add(exist_user.getId());
                       // newtask.setStatus(taskDto.getStatus());
                    }
                    // userRepository.save(exist_user);
                } else {
                    taskservicelogger.warn("User not found with ID: {}", taskDto.getAssignedTo().get(i));
                    System.out.println("User not found with id" + taskDto.getAssignedTo().get(i));
                }
            }
            newtask.setAssignedTo(usersList);
            taskservicelogger.info("Task successfully created with ID: {}", newtask.getId());
            taskRepository.save(newtask);
            TaskDisplayDto TD1 = new TaskDisplayDto();
            TD1.setId(newtask.getId());
            TD1.setStatus(newtask.getStatus());
            TD1.setTitle(newtask.getTitle());
            TD1.setAssignedTo(stringList);
            TD1.setDueDate(newtask.getDueDate());
            TD1.setCreatedBy(newtask.getCreatedBy());
            return TD1;
        }


    @Override
    public void deletetask(String id) {


//        Optional<Tasks> optionaltask = taskRepository.findById(id);
//        if(!optionaltask.isEmpty()){
//            Tasks existtask=optionaltask.get();
//            List<Users> usersList
//        }

        taskservicelogger.info("Deleting task with ID: {}", id);
        taskRepository.deleteById(id);
        taskservicelogger.info("Task with ID: {} successfully deleted", id);
    }

    @Override
    public String updatetask(TaskUpdateDto taskupdateDto) {
        taskservicelogger.info("Updating task with ID: {}", taskupdateDto.getId());

        Optional<Tasks> optionaltask = taskRepository.findById(taskupdateDto.getId());
        if(optionaltask.isPresent()){
            Tasks exist_task=optionaltask.get();
            exist_task.setStatus(taskupdateDto.getStatus());
            taskRepository.save(exist_task);
            return "Success";
        }else{
            System.out.println("task not found");
            return "Task not found";
        }
//        List<Users> usersList=new ArrayList<>();
//        if(!optionaltask.isEmpty()){
//            Tasks newtask=optionaltask.get();
//            usersList=newtask.getAssignedTo();
//            for(int i=0;i<taskupdateDto.getAssignedTo().size();i++){
//                Optional<Users> optionaluser=userRepository.findById(taskupdateDto.getAssignedTo().get(i));
//                if(!optionaluser.isEmpty()){
//                    Users exist_user=optionaluser.get();
////                    List<Tasks> tasksList=exist_user.getAsignedtasks();
////                    tasksList.add(newtask);
//
//                    if(!usersList.contains(exist_user)) {
//                        usersList.add(exist_user);
//                    }
//                  //  userRepository.save(exist_user);
//                   // usersList.add(optionaluser.get());
//                }else{
//                    taskservicelogger.warn("User not found with ID: {}", taskupdateDto.getAssignedTo().get(i));
//                    System.out.println("User not found with id"+taskupdateDto.getAssignedTo().get(i));
//                }
//            }
//
//            newtask.setAssignedTo(usersList);
//            newtask.setStatus("Assigned");
//            newtask.setTitle(taskupdateDto.getTitle());
//            newtask.setDueDate(taskupdateDto.getDueDate());
//            taskRepository.save(newtask);
//            taskservicelogger.info("Task with ID: {} successfully updated", taskupdateDto.getId());
//
//
//        }else{
//            taskservicelogger.error("Task not found with ID: {}", taskupdateDto.getId());
//            System.out.println("Task not found");
//            throw new RuntimeException("Task not found");
//
//
//        }

    }

    @Override
    public String findtaskbyname(String taskname) {
        taskservicelogger.info("Finding task by name",taskname);
        Optional<Tasks> alreadyexisttask = taskRepository.findByTitle(taskname);
if(alreadyexisttask.isPresent()){
    taskservicelogger.info("Got the task returning id");
    return alreadyexisttask.get().getId();
}
taskservicelogger.warn("Task not present");
        return null;
    }

    @Override
    public List<TaskDto> getalltasksbyuserid(String id) {
        System.out.println(id);
        taskservicelogger.info("Entering");
        Optional<Users> optionaluser = userRepository.findById(id);
        List<TaskDto> result=new ArrayList<>();
        if(optionaluser.isPresent()) {
            Users user = optionaluser.get();
            taskservicelogger.info("User id", user.getId());
            List<Tasks> L1 = user.getAssignedTasks();
            for (int i = 0; i < L1.size(); i++) {
                TaskDto TD1 = new TaskDto();
                TD1.setDueDate(L1.get(i).getDueDate());
                TD1.setStatus(L1.get(i).getStatus());
                TD1.setTitle(L1.get(i).getTitle());
                TD1.setId(L1.get(i).getId());
                TD1.setCreatedBy(L1.get(i).getCreatedBy());
                result.add(TD1);
                taskservicelogger.info("message", TD1.getStatus());
            }
        }
            return result;
        }
    }

