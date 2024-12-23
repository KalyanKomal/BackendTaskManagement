package com.example.TaskManagement.ServiceImpl;

import com.example.TaskManagement.Dto.TaskDisplayDto;
import com.example.TaskManagement.Dto.TaskDto;
import com.example.TaskManagement.Entity.Tasks;
import com.example.TaskManagement.Entity.Users;
import com.example.TaskManagement.Repository.TaskRepository;
import com.example.TaskManagement.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @InjectMocks
private TaskServiceImpl taskServiceimpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Test
    void getAllTasks() {
        List<Tasks> tasksList=new ArrayList<>();
        Tasks task1 = new Tasks();
        task1.setId(UUID.randomUUID().toString());
        task1.setStatus("Pending");
        task1.setTitle("Task 1");
        task1.setCreatedBy("Kalyan");
        task1.setDueDate(Timestamp.valueOf(LocalDateTime.now()));
        List<Users> usersList=new ArrayList<>();
        task1.setAssignedTo(usersList);

        Tasks task2 = new Tasks();
        task2.setId(UUID.randomUUID().toString());
        task2.setStatus("Completed");
        task2.setTitle("Task 2");
        task2.setCreatedBy("Komal");
        task2.setDueDate(Timestamp.valueOf(LocalDateTime.now()));
        task2.setAssignedTo(usersList);
        tasksList=Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasksList);

        List<TaskDisplayDto> result = taskServiceimpl.getAllTasks();

        assertNotNull(result);
        assertEquals(2, result.size());

        TaskDisplayDto task1Dto = result.get(0);
        assertEquals("Pending", task1Dto.getStatus());
        assertEquals("Task 1", task1Dto.getTitle());
        assertEquals("Kalyan", task1Dto.getCreatedBy());
        TaskDisplayDto task2Dto = result.get(1);
        assertEquals("Completed", task2Dto.getStatus());
        assertEquals("Task 2", task2Dto.getTitle());
        assertEquals("Komal", task2Dto.getCreatedBy());

    }

    @Test
    void getbyid() {
        Tasks task1 = new Tasks();
        task1.setId("2345");
        task1.setStatus("Pending");
        task1.setTitle("Task 1");
        task1.setCreatedBy("Kalyan");
        task1.setDueDate(Timestamp.valueOf(LocalDateTime.now()));
        List<Users> usersList=new ArrayList<>();
        task1.setAssignedTo(usersList);
        when(taskRepository.findById("2345")).thenReturn(Optional.of(task1));

        Tasks result = taskServiceimpl.getbyid("2345");

        assertNotNull(result);
        assertEquals("2345", result.getId());
        assertEquals("Task 1", result.getTitle());
        when(taskRepository.findById("4567")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            taskServiceimpl.getbyid("4567");
        });

        assertEquals("Task not found", thrown.getMessage());
    }


    @Test
    void createtask() {
        Tasks task1 = new Tasks();
        task1.setId("2345");
        task1.setStatus("Pending");
        task1.setTitle("Test Task");
        task1.setCreatedBy("Kalyan");
        task1.setDueDate(Timestamp.valueOf(LocalDateTime.now()));
        List<Users> usersList=new ArrayList<>();
        task1.setAssignedTo(usersList);
        Users existingUser = new Users();
        existingUser.setId("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        existingUser.setUsername("User1");
        existingUser.setRole("Admin");
        existingUser.setEmail("User1@gmail.com");
        List<Tasks> tasksList=new ArrayList<>();
        existingUser.setAssignedTasks(tasksList);
         TaskDisplayDto taskDto = new TaskDisplayDto();
        taskDto.setTitle("Test Task");
        taskDto.setCreatedBy("Admin");
        taskDto.setStatus("Pending");
        taskDto.setDueDate(Timestamp.valueOf(LocalDateTime.now()));
        taskDto.setAssignedTo(Arrays.asList("user1", "user2"));
        when(taskRepository.findByTitle("Test Task")).thenReturn(Optional.empty());
        when(userRepository.findById("user1")).thenReturn(Optional.of(existingUser));
        when(userRepository.findById("user2")).thenReturn(Optional.of(existingUser));

        TaskDisplayDto result = taskServiceimpl.createtask(taskDto);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Pending", result.getStatus());

        when(taskRepository.findByTitle("Test Task")).thenReturn(Optional.of(task1));
        TaskDisplayDto resultExist = taskServiceimpl.createtask(taskDto);

        assertNotNull(resultExist);
        assertEquals("Test Task", resultExist.getTitle());
        assertEquals("Pending", resultExist.getStatus());

        when(userRepository.findById("user2")).thenReturn(Optional.empty());

        TaskDisplayDto resultMissingUser = taskServiceimpl.createtask(taskDto);

        assertNotNull(resultMissingUser);
        assertEquals(1, resultMissingUser.getAssignedTo().size());

    }

    @Test
    void deletetask() {
        doNothing().when(taskRepository).deleteById("2345");
        taskServiceimpl.deletetask("2345");
        verify(taskRepository, times(1)).deleteById("2345");
    }

    @Test
    void findtaskbyname() {
        Tasks task1 = new Tasks();
        task1.setId("2345");
        task1.setStatus("Pending");
        task1.setTitle("Task 1");
        task1.setCreatedBy("Kalyan");
        task1.setDueDate(Timestamp.valueOf(LocalDateTime.now()));
        List<Users> usersList=new ArrayList<>();
        task1.setAssignedTo(usersList);
        when(taskRepository.findByTitle("Task 1")).thenReturn(Optional.of(task1));

        String result = taskServiceimpl.findtaskbyname("Task 1");

        assertEquals("2345", result);
        verify(taskRepository, times(1)).findByTitle("Task 1");

        when(taskRepository.findByTitle("Task 1")).thenReturn(Optional.empty());

        result = taskServiceimpl.findtaskbyname("Task 1");

        assertNull(result);
        verify(taskRepository, times(2)).findByTitle("Task 1");
    }

    @Test
    void getalltasksbyuserid() {
        Tasks task1 = new Tasks();
        task1.setId("task1");
        task1.setStatus("In Progress");
        task1.setTitle("Task 1");
        task1.setCreatedBy("user1");
        task1.setDueDate(Timestamp.valueOf(LocalDateTime.now()));

        Tasks task2 = new Tasks();
        task2.setId("task2");
        task2.setStatus("Completed");
        task2.setTitle("Task 2");
        task2.setCreatedBy("user2");
        task2.setDueDate(Timestamp.valueOf(LocalDateTime.now()));
        Users existingUser = new Users();
        existingUser.setId("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
        existingUser.setUsername("User1");
        existingUser.setRole("Admin");
        existingUser.setEmail("User1@gmail.com");
        List<Tasks> tasksList=new ArrayList<>();
        existingUser.setAssignedTasks(tasksList);
        existingUser.setAssignedTasks(Arrays.asList(task1, task2));
        when(userRepository.findById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21")).thenReturn(Optional.of(existingUser));

        List<TaskDto> result = taskServiceimpl.getalltasksbyuserid("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");

        assertEquals(2, result.size());
        assertEquals("task1", result.get(0).getId());
        assertEquals("In Progress", result.get(0).getStatus());
        assertEquals("Task 1", result.get(0).getTitle());

        assertEquals("task2", result.get(1).getId());
        assertEquals("Completed", result.get(1).getStatus());
        assertEquals("Task 2", result.get(1).getTitle());

        existingUser.setAssignedTasks(new ArrayList<>());
        when(userRepository.findById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21")).thenReturn(Optional.of(existingUser));

        result = taskServiceimpl.getalltasksbyuserid("327b50e7-7ded-434b-8ef3-b9fd0fde4e21"   );

        assertTrue(result.isEmpty());

        when(userRepository.findById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21")).thenReturn(Optional.empty());

        result = taskServiceimpl.getalltasksbyuserid("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");

        assertTrue(result.isEmpty());

        verify(userRepository, times(3)).findById("327b50e7-7ded-434b-8ef3-b9fd0fde4e21");
    }

}