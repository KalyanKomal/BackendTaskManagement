package com.example.TaskManagement.ServiceImpl;

import com.example.TaskManagement.Dto.*;
import com.example.TaskManagement.Entity.Notifications;
import com.example.TaskManagement.Entity.Tasks;
import com.example.TaskManagement.Entity.Users;
import com.example.TaskManagement.Repository.NotificationRepository;
import com.example.TaskManagement.Repository.TaskRepository;
import com.example.TaskManagement.Repository.UserRepository;
import com.example.TaskManagement.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    private static final Logger userservicelogger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public List<UserDisplayDto> getAllUsers() {
        userservicelogger.info("Fetching all users from repository");
        List<Users> usersList=new ArrayList<>();
        usersList=userRepository.findAll();
        List<UserDisplayDto> L1=new ArrayList<>();
        for(int i=0;i<usersList.size();i++){
            Users new_user=usersList.get(i);
            UserDisplayDto userDisplayDto=new UserDisplayDto();
            userDisplayDto.setId(new_user.getId());
            userDisplayDto.setRole(new_user.getRole());
            List<String>taskids=new ArrayList<>();
            for(int j=0;j<new_user.getAssignedTasks().size();j++){
                taskids.add(new_user.getAssignedTasks().get(j).getTitle());
            }
            userDisplayDto.setAsignedtasksids(taskids);
            L1.add(userDisplayDto);
        }
        userservicelogger.info("Fetched {} users", L1.size());

        return L1;
    }

    @Override
    public Users getbyid(String id) {
        userservicelogger.info("Fetching user by id: {}", id);
        Optional<Users> user=userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            userservicelogger.error("User not found with id: {}", id);
            throw new RuntimeException("Task not found");
        }
    }

    @Override
    public Users createuser(admincreateuserDto userdto) {
        userservicelogger.info("Creating new user");
        Users newuser=new Users();
        newuser.setId(UUID.randomUUID().toString());
      //  List<Tasks> tasklist=new ArrayList<>();
//        for(int i=0;i<userdto.getAsignedtasks().size();i++){
//            Optional<Tasks> tasks = taskRepository.findById(userdto.getAsignedtasks().get(i));
//            if(!tasks.isEmpty()){
//                Tasks existtask=tasks.get();
//                tasklist.add(existtask);
//                existtask.setStatus("Assigned");
//                List<Users> usersList=existtask.getAssignedTo();
//                usersList.add(newuser);
//                taskRepository.save(existtask);
//            }
//        }
//        newuser.setAsignedtasks(tasklist);
        newuser.setRole(userdto.getRole());
        newuser.setUsername(userdto.getUsername());
       // newuser.setPassword(userdto.getPassword());
       // newuser.setUsername(userdto.getUsername());
        newuser.setEmail(userdto.getEmail());
        //newuser.setNotificationsList(userdto.getNotificationsList());
        userservicelogger.info("User created with id: {}", newuser.getId());

        return userRepository.save(newuser);

    }

    @Override
    public void deleteuser(String id) {
        Optional<Users> optionaluser = userRepository.findById(id);
        if(!optionaluser.isEmpty()){
            Users exist_user=optionaluser.get();
            exist_user.getAssignedTasks().clear();
            userRepository.save(exist_user);
//            for(int i=0;i<exist_user.getAsignedtasks().size();i++){
//                Optional<Tasks> optionnaltask = taskRepository.findById(exist_user.getAsignedtasks().get(i).getId());
//                if(!optionnaltask.isEmpty()){
//                    List<Users> assignedTo = optionnaltask.get().getAssignedTo();
//                    if(!assignedTo.isEmpty()) {
//                        assignedTo.remove(exist_user);
//                    }
//                    if(assignedTo.size()==0){
//                        optionnaltask.get().setStatus("Not assigned");
//                    }
//                    taskRepository.save(optionnaltask.get());
//                }
//            }
        }

        userRepository.deleteById(id);

    }

    @Override
    public void updatebyadmin(AdminUpdateDto adminUpdateDto) {
        userservicelogger.info("Updating user by admin with id: {}", adminUpdateDto.getId());
        Optional<Users> existuser = userRepository.findById(adminUpdateDto.getId());
       // List<Tasks> tasksList=new ArrayList<>();
        if(!existuser.isEmpty()){
            Users users=existuser.get();
            users.setRole(adminUpdateDto.getRole());
//            for(int i=0;i<adminUpdateDto.getTasksid().size();i++){
//                Optional<Tasks> optionalexisttask = taskRepository.findById(adminUpdateDto.getTasksid().get(i));
//                if(!optionalexisttask.isEmpty()){
//                    Tasks existtask=optionalexisttask.get();
//                    tasksList.add(existtask);
//                    existtask.setStatus("Assigned");
//                    List<Users> usersList=existtask.getAssignedTo();
//                    usersList.add(users);
//                    taskRepository.save(existtask);
//                }else{
//                    System.out.println("Task not found with id"+adminUpdateDto.getTasksid().get(i));
//                }
            //}
           // users.setAsignedtasks(tasksList);
            userservicelogger.info("User updated by admin with id: {}", adminUpdateDto.getId());

            userRepository.save(users);
        }else{
            userservicelogger.warn("User not found with id: {}", adminUpdateDto.getId());

            System.out.println("User not found with id"+adminUpdateDto.getId());
        }
    }

    @Override
    public String updatebyuser(UserupdateDto userupdateDto) {
        userservicelogger.info("Updating user with email: {}", userupdateDto.getEmail());

        Optional<Users> optionalexistuser = userRepository.findByemail(userupdateDto.getEmail());
        if(!optionalexistuser.isEmpty()){
            Users existuser=optionalexistuser.get();
            String oldpass=existuser.getPassword();
            if(oldpass.equals(userupdateDto.getOldpassword())){
                existuser.setPassword(userupdateDto.getNewpassword());
                userRepository.save(existuser);
                return "Success";
            }else{
                userservicelogger.info("User wrong password: {}", userupdateDto.getOldpassword());
                return "Wrong password";
            }
//            existuser.setUsername(userupdateDto.getUsername());
//            existuser.setPassword(userupdateDto.getPassword());

        }else{
            userservicelogger.warn("User not found with email: {}", userupdateDto.getEmail());
            //System.out.println("User not found with id"+userupdateDto.getId());
            return "User not found";
        }
    }

    @Override
    public String checkcredentials(loginDto LoginDto) {
        userservicelogger.info("Checking user credentials", LoginDto);

        Optional<Users> user = userRepository.findByemail(LoginDto.getEmail());
        if(user.isPresent()) {
            userservicelogger.info("User is", user.get());
            if (user != null && LoginDto.getPassword().equals(user.get().getPassword())) {
                //UserDisplayDto UDD1 = new UserDisplayDto();
                userservicelogger.info("User found");
                return user.get().getRole();
            }else{
                userservicelogger.warn("Password not found");
                return "Password not found";
            }
        }
        userservicelogger.warn("User not found");
        return "Email not found";
    }

    @Override
    public String checksignup(signupdto Signupdto) {
        userservicelogger.info("Checking sign up details",Signupdto);
        Optional<Users> user = userRepository.findByemail(Signupdto.getEmail());
         if(user.isPresent()){
             if(user.get().getPassword()==null) {
                 String password = Signupdto.getPassword();
                 Users U1 = user.get();
                 U1.setPassword(password);
                 userservicelogger.info("Everthing good saving new user");
                 userRepository.save(U1);
                 return "Success";
             }else{
                 userservicelogger.warn("Password already exists");
                 return "Password already exist";
             }
         }
         userservicelogger.error("Not authorized");
        return "Not authorized";
    }

    @Override
    public List<UserDropdownDto> getusersdropdown() {
        userservicelogger.info("Fetching user deatils from repository for dropdown");
        List<Users> L1=userRepository.findAll();
        List<UserDropdownDto> result=new ArrayList<>();
        for(int i=0;i<L1.size();i++){
            if(L1.get(i).getRole().equals("User")) {
                UserDropdownDto UD1 = new UserDropdownDto();
                UD1.setId(L1.get(i).getId());
                UD1.setUsername(L1.get(i).getUsername());
                UD1.setEmail(L1.get(i).getEmail());
                result.add(UD1);
            }
        }
        return result;
    }

    @Override
    public String getbyemail(String email) {
        userservicelogger.info("Fecthing user with email",email);
        Optional<Users> user = userRepository.findByemail(email);
        if(user.isPresent()){
            userservicelogger.info("User found");
            return user.get().getId();
        }
        userservicelogger.warn("User not found");
return null;
    }

    @Override
    public String emailsending(EmailSendingDto emailSendingDto) {
        userservicelogger.info("Enterd into method creating mail");
        for(int i=0;i<emailSendingDto.getEmailids().size();i++) {
Notifications newnotifications=new Notifications();
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(emailSendingDto.getUseremail());
                message.setTo(emailSendingDto.getEmailids().get(i));
//System.out.println(emailSendingDto.getCreatedBy());
                message.setSubject(emailSendingDto.getCreatedBy());
//                System.out.println(" sending email date: " + emailSendingDto.getDueDate()+"and it's due date"+emailSendingDto.);



                message.setText(emailSendingDto.getTitle()+"has been assigned to you and its due date is "+emailSendingDto.getDueDate());
                javaMailSender.send(message);
                System.out.println("Email sent to: " + emailSendingDto.getEmailids().get(i));
                Optional<Users> existuser=userRepository.findByemail(emailSendingDto.getEmailids().get(i));
                if(existuser.isPresent()){
                    newnotifications.setUser_id(existuser.get());
                    newnotifications.setTime(Timestamp.valueOf(LocalDateTime.now()));
                    newnotifications.setMessage(emailSendingDto.getTitle());
                    newnotifications.setId(UUID.randomUUID().toString());
                    userservicelogger.info("Created notification");
                    notificationRepository.save(newnotifications);
                }
            } catch (Exception e) {
                System.out.println("Error sending email: " + e.getMessage());
                return "Error";
            }
        }
        return "Success";
    }
}
