package com.example.TaskManagement.Service;

import com.example.TaskManagement.Dto.*;
import com.example.TaskManagement.Entity.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public List<UserDisplayDto> getAllUsers();
    public Users getbyid(String id);
    public Users createuser(admincreateuserDto userdto);
    public void deleteuser(String id);
    public void updatebyadmin(AdminUpdateDto adminUpdateDto);

   public  String updatebyuser(UserupdateDto userupdateDto);
   public String checkcredentials(loginDto LoginDto);

   public  String checksignup(signupdto Signupdto);

  public List<UserDropdownDto> getusersdropdown();

   public  String getbyemail(String email);

   public  String emailsending(EmailSendingDto emailSendingDto);
}
