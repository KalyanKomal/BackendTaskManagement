package com.example.TaskManagement.Controller;

import com.example.TaskManagement.Dto.*;
import com.example.TaskManagement.Entity.Users;
import com.example.TaskManagement.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
      private static final Logger usercontrollerlogger=LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/getallusers")
    public List<UserDisplayDto>getallusers(){
        usercontrollerlogger.info("Fetching all users");
        return userService.getAllUsers();
    }
    @GetMapping("/getusersdropdown")
    public List<UserDropdownDto>getusersdropdown(){
        usercontrollerlogger.info("Fetching all users");
        return userService.getusersdropdown();
    }
    @PostMapping("/createuser")
    public Users createuser(@RequestBody admincreateuserDto userdto){
        usercontrollerlogger.info("Creating new user"+ userdto);
        return userService.createuser(userdto);

    }

    @GetMapping("/getuserbyid/{id}")
    public Users getuserbyid(@PathVariable String id){
        usercontrollerlogger.info("Getting details of user with id"+id);
        return userService.getbyid(id);
    }
    @GetMapping("/getuserbyemail/{email}")
    public ResponseEntity<Map> getuserbyemail(@PathVariable String email){
        usercontrollerlogger.info("Getting details of user with email"+email);
        String result=userService.getbyemail(email);
        if(result!=null){
            return ResponseEntity.ok(Map.of("message", result));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "User is not present"));
        }
    }

    @DeleteMapping("/deleteuserbyid/{id}")
    public void deleteuserbyid(@PathVariable String id){
        usercontrollerlogger.info("Deleting user with the id"+id);
        userService.deleteuser(id);
    }

    @PostMapping("/updatebyadmin")
    public void updatebyadmin(@RequestBody AdminUpdateDto adminUpdateDto){
usercontrollerlogger.info("Updating the user with id"+adminUpdateDto.getId());
        userService.updatebyadmin(adminUpdateDto);
    }

    @PutMapping("/updatebyuser")
    public ResponseEntity<Map> updatebyuser(@RequestBody UserupdateDto userupdateDto) {
        usercontrollerlogger.info("Updating the user with email" + userupdateDto.getEmail());
        String result = userService.updatebyuser(userupdateDto);
        if (result.equals("Success")) {
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", result));

        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody loginDto logindto){
        String result=userService.checkcredentials(logindto);
        if ("Admin".equals(result) || "User".equals(result)) {
            return ResponseEntity.ok(Map.of("role", result));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", result));
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<Map> signup(@RequestBody signupdto Signupdto){
        String result=userService.checksignup(Signupdto);
        if ("Success".equals(result)) {
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", result));
        }
    }
    @PostMapping("/emailsending")
    public ResponseEntity<Map> emailsendoing(@RequestBody EmailSendingDto emailSendingDto){
       String result=userService.emailsending(emailSendingDto);
        if ("Success".equals(result)) {
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", result));
        }
    }

}
