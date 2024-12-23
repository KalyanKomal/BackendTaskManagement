package com.example.TaskManagement.ServiceImpl;

import com.example.TaskManagement.Dto.*;
import com.example.TaskManagement.Entity.Notifications;
import com.example.TaskManagement.Entity.Tasks;
import com.example.TaskManagement.Entity.Users;
import com.example.TaskManagement.Repository.NotificationRepository;
import com.example.TaskManagement.Repository.TaskRepository;
import com.example.TaskManagement.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userserviceimpl;

    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private NotificationRepository notificationRepository;



    @Test
    void getbyid() {
        String userId = "327b50e7-7ded-434b-8ef3-b9fd0fde4e21";
        Users testUser = new Users();
        testUser.setId(userId);
        testUser.setUsername("User1");
        testUser.setPassword("2003");
        testUser.setEmail("User1@gmail.com");
        List<Tasks> tasksList=new ArrayList<>();
        testUser.setAssignedTasks(tasksList);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        Users result = userserviceimpl.getbyid(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("User1@gmail.com", result.getEmail());

    }

    @Test
    void createuser() {
        admincreateuserDto userDto=new admincreateuserDto();
userDto.setId("");
        userDto.setUsername("User1");
        userDto.setRole("Admin");
        userDto.setEmail("User1@gmail.com");


        Users expectedUser = new Users();
        expectedUser.setId(UUID.randomUUID().toString());
        expectedUser.setUsername("User1");
        expectedUser.setRole("Admin");
        expectedUser.setEmail("User1@gmail.com");

        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(expectedUser);

        Users result = userserviceimpl.createuser(userDto);

        assertNotNull(result);
        assertEquals("User1", result.getUsername());
        assertEquals("Admin", result.getRole());
        assertEquals("User1@gmail.com", result.getEmail());
        assertNotNull(result.getId());

    }

    @Test
    void deleteuser() {
        Users existingUser = new Users();
        existingUser.setId("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        existingUser.setUsername("User1");
        existingUser.setRole("Admin");
        existingUser.setEmail("User1@gmail.com");
        List<Tasks> tasksList=new ArrayList<>();
        existingUser.setAssignedTasks(tasksList);

        Mockito.when(userRepository.findById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21")).thenReturn(Optional.of(existingUser));
        Mockito.doNothing().when(userRepository).deleteById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");

        userserviceimpl.deleteuser("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");

        Mockito.verify(userRepository, times(1)).findById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        Mockito.verify(userRepository, times(1)).save(existingUser);
        Mockito.verify(userRepository, times(1)).deleteById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");

        assertTrue(existingUser.getAssignedTasks().isEmpty());
    }

    @Test
    void updatebyuser() {
        UserupdateDto userupdateDto=new UserupdateDto();
        userupdateDto.setEmail("User1@gmail.com");
        userupdateDto.setOldpassword("1000");
        userupdateDto.setNewpassword("2003");
        Users existingUser=new Users();
        existingUser.setId("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        existingUser.setUsername("User1");
        existingUser.setRole("Admin");
        existingUser.setEmail("User1@gmail.com");
        existingUser.setPassword("1000");
        List<Tasks> tasksList=new ArrayList<>();
        existingUser.setAssignedTasks(tasksList);

        when(userRepository.findByemail(userupdateDto.getEmail())).thenReturn(Optional.of(existingUser));
        String result = userserviceimpl.updatebyuser(userupdateDto);
        assertEquals("Success", result);
        assertEquals("2003", existingUser.getPassword());
        verify(userRepository).save(existingUser);

        reset(userRepository);

        userupdateDto.setOldpassword("1002");
        when(userRepository.findByemail(userupdateDto.getEmail())).thenReturn(Optional.of(existingUser));
        result = userserviceimpl.updatebyuser(userupdateDto);
        assertEquals("Wrong password", result);

        reset(userRepository);

        when(userRepository.findByemail(userupdateDto.getEmail())).thenReturn(Optional.empty());
        result = userserviceimpl.updatebyuser(userupdateDto);
        assertEquals("User not found", result);
    }

    @Test
    void checkcredentials() {
        loginDto LD=new loginDto();
LD.setEmail("User1@gmail.com");
LD.setPassword("1000");
        Users existingUser=new Users();
        existingUser.setId("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        existingUser.setUsername("User1");
        existingUser.setRole("Admin");
        existingUser.setEmail("User1@gmail.com");
        existingUser.setPassword("1000");
        when(userRepository.findByemail(LD.getEmail())).thenReturn(Optional.of(existingUser));

        String result = userserviceimpl.checkcredentials(LD);

        assertEquals("Admin", result);

        reset(userRepository);
        when(userRepository.findByemail(LD.getEmail())).thenReturn(Optional.of(existingUser));
        LD.setPassword("3000");

         result = userserviceimpl.checkcredentials(LD);

        assertEquals("Password not found", result);
        reset(userRepository);
        when(userRepository.findByemail(LD.getEmail())).thenReturn(Optional.empty());
         result = userserviceimpl.checkcredentials(LD);
        assertEquals("Email not found", result);

    }

    @Test
    void checksignup() {

        signupdto SD=new signupdto();
        SD.setEmail("User1@gmail.com");
        SD.setPassword("1000");


        Users existingUser=new Users();
        existingUser.setId("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        existingUser.setUsername("User1");
        existingUser.setRole("Admin");
        existingUser.setEmail("User1@gmail.com");
        existingUser.setPassword(null);
        when(userRepository.findByemail(SD.getEmail())).thenReturn(Optional.of(existingUser));
        String result = userserviceimpl.checksignup(SD);
        assertEquals("Success", result);
        assertEquals("1000", existingUser.getPassword());
reset(userRepository);
        existingUser.setPassword("2003");
        when(userRepository.findByemail(SD.getEmail())).thenReturn(Optional.of(existingUser));
        result = userserviceimpl.checksignup(SD);
        assertEquals("Password already exist", result);

        reset(userRepository);


        when(userRepository.findByemail(SD.getEmail())).thenReturn(Optional.empty());
        result = userserviceimpl.checksignup(SD);
        assertEquals("Not authorized", result);
    }

    @Test
    void getusersdropdown() {
        List<Users>usersList = new ArrayList<>();
        Users user1 = new Users();
        user1.setId(UUID.randomUUID().toString());
        user1.setUsername("User1");
        user1.setEmail("user1@gmail.com");
        user1.setRole("User");
        user1.setPassword("2001");
usersList.add(user1);
        Users user2 = new Users();
        user2.setId(UUID.randomUUID().toString());
        user2.setUsername("User2");
        user2.setEmail("user2@gmail.com");
        user2.setRole("Admin");
        user2.setPassword("2002");
        usersList.add(user2);
        when(userRepository.findAll()).thenReturn(usersList);

        List<UserDropdownDto> result = userserviceimpl.getusersdropdown();

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getUsername().equals("User1")));


    }

    @Test
    void getbyemail() {
        Users existingUser=new Users();
        existingUser.setId("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        existingUser.setUsername("User1");
        existingUser.setRole("Admin");
        existingUser.setEmail("User1@gmail.com");
        existingUser.setPassword("2003");
        when(userRepository.findByemail("User1@gmail.com")).thenReturn(Optional.of(existingUser));
        String resultFound = userserviceimpl.getbyemail("User1@gmail.com");
        assertNotNull(resultFound);
        assertEquals("327b50e7-7ded-434b-8ef3-b9fd0fde4e21", resultFound);

        when(userRepository.findByemail("user2@gmail.com")).thenReturn(Optional.empty());
        String resultNotFound = userserviceimpl.getbyemail("user2@gmail.com");
        assertNull(resultNotFound);
    }
}