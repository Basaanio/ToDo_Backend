package ideas.ToDo_Application.todo_data;
import ideas.ToDo_Application.todo_data.entity.ApplicationUser;
import ideas.ToDo_Application.todo_data.entity.CollaboratorsTask;
import ideas.ToDo_Application.todo_data.entity.Task;
import ideas.ToDo_Application.todo_data.exception.CollaboratorTaskNotFoundException;
import ideas.ToDo_Application.todo_data.repository.ApplicationUserRepository;
import ideas.ToDo_Application.todo_data.repository.CollaboratorsTaskRepository;
import ideas.ToDo_Application.todo_data.repository.TaskRepository;
import ideas.ToDo_Application.todo_data.service.CollaboratorsTaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.List; // <-- Import List


import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CollaboratorsTaskServiceTest {

    @InjectMocks
    private CollaboratorsTaskService collaboratorsTaskService;

    @Mock
    private CollaboratorsTaskRepository collaboratorsTaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test Case for addTaskWithCollaborators
    @Test
    public void testAddTaskWithCollaborators_Success() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1);
        Integer creatorId = 1;
        List<Integer> collaboratorIds = Arrays.asList(2, 3);

        ApplicationUser creator = new ApplicationUser();
        creator.setUserId(1);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(applicationUserRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        when(applicationUserRepository.findById(2)).thenReturn(Optional.of(new ApplicationUser()));
        when(applicationUserRepository.findById(3)).thenReturn(Optional.of(new ApplicationUser()));

        // Act
        collaboratorsTaskService.addTaskWithCollaborators(task, creatorId, collaboratorIds);

        // Assert
        verify(taskRepository).save(task);
        verify(collaboratorsTaskRepository, times(3)).save(any(CollaboratorsTask.class)); // 1 creator + 2 collaborators
    }

    // Negative Test Case for addTaskWithCollaborators (Collaborator Not Found)
    @Test
    public void testAddTaskWithCollaborators_CollaboratorNotFound() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1);
        Integer creatorId = 1;
        List<Integer> collaboratorIds = Arrays.asList(2);

        ApplicationUser creator = new ApplicationUser();
        creator.setUserId(1);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(applicationUserRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        when(applicationUserRepository.findById(2)).thenReturn(Optional.empty()); // Collaborator not found

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            collaboratorsTaskService.addTaskWithCollaborators(task, creatorId, collaboratorIds);
        });

        assertEquals("Collaborator not found", exception.getMessage());
    }


    // Positive Test Case for getCollaboratorsTaskById
    @Test
    public void testGetCollaboratorsTaskById_Success() {
        // Arrange
        CollaboratorsTask collaboratorsTask = new CollaboratorsTask();
        collaboratorsTask.setCollaboratorsTaskId(1);
        when(collaboratorsTaskRepository.findById(1)).thenReturn(Optional.of(collaboratorsTask));

        // Act
        CollaboratorsTask result = collaboratorsTaskService.getCollaboratorsTaskById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCollaboratorsTaskId());
    }

    // Negative Test Case for getCollaboratorsTaskById (Not Found)
    @Test
    public void testGetCollaboratorsTaskById_NotFound() {
        // Arrange
        when(collaboratorsTaskRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CollaboratorTaskNotFoundException.class, () -> {
            collaboratorsTaskService.getCollaboratorsTaskById(1);
        });
    }

    // Positive Test Case for deleteCollaboratorsTask
    @Test
    public void testDeleteCollaboratorsTask_Success() {
        // Arrange
        when(collaboratorsTaskRepository.existsById(1)).thenReturn(true);

        // Act
        collaboratorsTaskService.deleteCollaboratorsTask(1);

        // Assert
        verify(collaboratorsTaskRepository).deleteById(1);
    }

    // Negative Test Case for deleteCollaboratorsTask (Not Found)
    @Test
    public void testDeleteCollaboratorsTask_NotFound() {
        // Arrange
        when(collaboratorsTaskRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(CollaboratorTaskNotFoundException.class, () -> {
            collaboratorsTaskService.deleteCollaboratorsTask(1);
        });
    }
}
