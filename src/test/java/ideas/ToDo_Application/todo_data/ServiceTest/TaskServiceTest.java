import ideas.ToDo_Application.todo_data.entity.Task;
import ideas.ToDo_Application.todo_data.entity.CollaboratorsTask;
import ideas.ToDo_Application.todo_data.entity.ApplicationUser;
import ideas.ToDo_Application.todo_data.exception.UserNotFoundException;
import ideas.ToDo_Application.todo_data.repository.ApplicationUserRepository;
import ideas.ToDo_Application.todo_data.repository.CollaboratorsTaskRepository;
import ideas.ToDo_Application.todo_data.service.TaskService;
import ideas.ToDo_Application.todo_data.dto.TaskDTO;
import ideas.ToDo_Application.todo_data.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private CollaboratorsTaskRepository collaboratorsTaskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasks() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);
        Task task1 = new Task(1, user, null, "Task 1", "Description 1", LocalDateTime.now(), Task.Priority.MEDIUM, Task.Status.PENDING);
        Task task2 = new Task(2, user, null, "Task 2", "Description 2", LocalDateTime.now(), Task.Priority.HIGH, Task.Status.COMPLETED);
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));
        List<TaskDTO> result = taskService.getAllTasks();
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testConvertToDTO_WithCollaborators() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);
        CollaboratorsTask collabTask = new CollaboratorsTask();
        collabTask.setCollaboratorUser(new ApplicationUser());
        collabTask.getCollaboratorUser().setUserId(2);
        Task task = new Task(1, user, Collections.singletonList(collabTask), "Task Title", "Task Description", LocalDateTime.now(), Task.Priority.HIGH, Task.Status.PENDING);
        TaskDTO dto = taskService.convertToDTO(task);
        assertNotNull(dto);
        assertEquals(1, dto.getTaskId());
        assertEquals("Task Title", dto.getTitle());
        assertTrue(dto.getIsCollaborative());
        assertEquals(1, dto.getCollaboratorIds().size());
        assertEquals(2, dto.getCollaboratorIds().get(0));
    }

    @Test
    public void testConvertToDTO_WithoutCollaborators() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);
        Task task = new Task(1, user, Collections.emptyList(), "Task Title", "Task Description", LocalDateTime.now(), Task.Priority.MEDIUM, Task.Status.COMPLETED);
        TaskDTO dto = taskService.convertToDTO(task);
        assertNotNull(dto);
        assertEquals(1, dto.getTaskId());
        assertEquals("Task Title", dto.getTitle());
        assertFalse(dto.getIsCollaborative());
        assertTrue(dto.getCollaboratorIds().isEmpty());
    }

    @Test
    public void testConvertToDTO_NullTask() {
        TaskDTO dto = taskService.convertToDTO(null);
        assertNull(dto);
    }

    @Test
    void testCreateTask_WithoutCollaborator() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Description of Test Task");
        taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
        taskDTO.setPriority("HIGH");
        taskDTO.setStatus("PENDING");
        taskDTO.setUserId(1);
        taskDTO.setIsCollaborative(false);

        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);
        user.setUsername("testuser");

        when(applicationUserRepository.findById(1)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setTaskId(1); // Set a dummy ID to simulate saving
            return task; // Return the same task for simplicity
        });

        // Act
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);

        // Assert
        assertNotNull(createdTaskDTO);
        assertEquals("Test Task", createdTaskDTO.getTitle());
        assertEquals("Description of Test Task", createdTaskDTO.getDescription());
    }


    @Test
    public void testCreateTask_UserNotFound() {
        TaskDTO taskDTO = new TaskDTO(null, "Task Title", "Task Description", LocalDateTime.now(), "MEDIUM", "PENDING", 1, false, null);

        when(applicationUserRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.createTask(taskDTO));

        verify(applicationUserRepository, times(1)).findById(1);
        verify(taskRepository, never()).save(any(Task.class));
    }

    // Updated test case for creating a task with collaborators
    @Test
    void testCreateTask_WithCollaborators() {
        // Arrange
        List<Integer> collaboratorIds = Arrays.asList(2, 3);
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Description of Test Task");
        taskDTO.setDueDate(LocalDateTime.now().plusDays(1));
        taskDTO.setPriority("HIGH");
        taskDTO.setStatus("PENDING");
        taskDTO.setUserId(1);
        taskDTO.setIsCollaborative(true);
        taskDTO.setCollaboratorIds(collaboratorIds);

        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);
        user.setUsername("testuser");

        ApplicationUser collaborator1 = new ApplicationUser();
        collaborator1.setUserId(2);
        collaborator1.setUsername("collaborator1");

        ApplicationUser collaborator2 = new ApplicationUser();
        collaborator2.setUserId(3);
        collaborator2.setUsername("collaborator2");

        when(applicationUserRepository.findById(1)).thenReturn(Optional.of(user));
        when(applicationUserRepository.findById(2)).thenReturn(Optional.of(collaborator1));
        when(applicationUserRepository.findById(3)).thenReturn(Optional.of(collaborator2));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setTaskId(1); // Simulating the ID being set on save
            return task; // Return the same task
        });

        // Act
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);

        // Assert
        assertNotNull(createdTaskDTO);
        assertEquals("Test Task", createdTaskDTO.getTitle());
        assertEquals("Description of Test Task", createdTaskDTO.getDescription());
        assertEquals(1, createdTaskDTO.getTaskId());
      //  assertTrue(createdTaskDTO.getIsCollaborative());
    }


    @Test
    public void testCreateTask_CollaboratorNotFound() {
        TaskDTO taskDTO = new TaskDTO(null, "Test Task", "Test Description", LocalDateTime.now(), "MEDIUM", "PENDING", 1, true, List.of(2));

        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);

        // Mocking the repository responses
        when(applicationUserRepository.findById(1)).thenReturn(Optional.of(user));
        when(applicationUserRepository.findById(2)).thenReturn(Optional.empty()); // Collaborator not found

        // Expect UserNotFoundException when a collaborator is missing
        assertThrows(UserNotFoundException.class, () -> taskService.createTask(taskDTO));

        verify(applicationUserRepository, times(2)).findById(anyInt()); // Ensure both user and collaborator are checked
        verify(taskRepository, never()).save(any(Task.class)); // Task should not be saved if collaborator is missing
        verify(collaboratorsTaskRepository, never()).save(any(CollaboratorsTask.class));
    }

}
