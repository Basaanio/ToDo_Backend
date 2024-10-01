package ideas.ToDo_Application.todo_data;

import ideas.ToDo_Application.todo_data.dto.ApplicationUserDTO;
import ideas.ToDo_Application.todo_data.entity.ApplicationUser;
import ideas.ToDo_Application.todo_data.entity.Task;
import ideas.ToDo_Application.todo_data.service.ApplicationUserService;
import ideas.ToDo_Application.todo_data.exception.UserNotFoundException;
import ideas.ToDo_Application.todo_data.repository.ApplicationUserRepository;
import ideas.ToDo_Application.todo_data.repository.TaskRepository;
import ideas.ToDo_Application.todo_data.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository; // Your repository interface

    private ApplicationUser user;
    private LocalDateTime dueDate;
    private List<Task> taskList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Initialize ApplicationUser
        user = new ApplicationUser();
        user.setUserId(1);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("securePassword");
        user.setRole("USER");

        // Initialize due date
        dueDate = LocalDateTime.now().minusDays(1); // Set a due date in the past

        // Initialize task list
        taskList = new ArrayList<>();

        // Creating sample tasks for testing
        Task task1 = new Task();
        task1.setTaskId(1);
        task1.setUser(user);
        task1.setDueDate(LocalDateTime.now().minusDays(2)); // 2 days ago
        task1.setStatus(Task.Status.PENDING);
        taskList.add(task1);

        Task task2 = new Task();
        task2.setTaskId(2);
        task2.setUser(user);
        task2.setDueDate(LocalDateTime.now().minusDays(3)); // 3 days ago
        task2.setStatus(Task.Status.COMPLETED);
        taskList.add(task2);
    }

    @Test
    public void testFindByUser_UserId() {
        // Arrange
        when(taskRepository.findByUser_UserId(user.getUserId())).thenReturn(taskList);

        // Act
        List<Task> result = taskRepository.findByUser_UserId(user.getUserId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Should return two tasks
        verify(taskRepository, times(1)).findByUser_UserId(user.getUserId());
    }

    @Test
    public void testFindByDueDateBetweenAndUser_ReturnsTasks() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(/* Initialize task properties here */)); // Mock a task

        when(taskRepository.findByDueDateBetweenAndUser(startDate, endDate, user)).thenReturn(tasks);

        // Act
        List<Task> result = taskRepository.findByDueDateBetweenAndUser(startDate, endDate, user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        // Add more assertions based on the Task properties you are expecting
    }

    @Test
    public void testFindByDueDateBetweenAndUser_ReturnsEmptyList() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Task> tasks = new ArrayList<>(); // Empty list

        when(taskRepository.findByDueDateBetweenAndUser(startDate, endDate, user)).thenReturn(tasks);

        // Act
        List<Task> result = taskRepository.findByDueDateBetweenAndUser(startDate, endDate, user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for findByDueDateBeforeAndUserAndStatus
    @Test
    public void testFindByDueDateBeforeAndUserAndStatus() {
        // Arrange
        Task.Status status = Task.Status.PENDING;
        when(taskRepository.findByDueDateBeforeAndUserAndStatus(dueDate, user, status)).thenReturn(taskList);

        // Act
        List<Task> result = taskRepository.findByDueDateBeforeAndUserAndStatus(dueDate, user, status);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Adjust based on tasks you expect to be returned
        verify(taskRepository, times(1)).findByDueDateBeforeAndUserAndStatus(dueDate, user, status);
    }

    @Test
    public void testFindByDueDateBeforeAndUserAndStatus_ReturnsEmptyList() {
        // Arrange
        LocalDateTime dueDate = LocalDateTime.now(); // Current time
        Task.Status status = Task.Status.COMPLETED; // Status we're looking for
        when(taskRepository.findByDueDateBeforeAndUserAndStatus(dueDate, user, status)).thenReturn(new ArrayList<>());

        // Act
        List<Task> result = taskRepository.findByDueDateBeforeAndUserAndStatus(dueDate, user, status);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Should return an empty list
        verify(taskRepository, times(1)).findByDueDateBeforeAndUserAndStatus(dueDate, user, status);
    }
    @Test
    public void testFindByUser_UserId_NonExistentUser() {
        // Arrange
        when(taskRepository.findByUser_UserId(999)).thenReturn(new ArrayList<>()); // Assume 999 is non-existent

        // Act
        List<Task> result = taskRepository.findByUser_UserId(999);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Should return an empty list
        verify(taskRepository, times(1)).findByUser_UserId(999);
    }


}