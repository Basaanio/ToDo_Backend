package ideas.ToDo_Application.todo_data.RepositoryTest;

import ideas.ToDo_Application.todo_data.dto.CollaboratorTaskProjection;
import ideas.ToDo_Application.todo_data.entity.ApplicationUser;
import ideas.ToDo_Application.todo_data.entity.CollaboratorsTask;
import ideas.ToDo_Application.todo_data.entity.Task;
import ideas.ToDo_Application.todo_data.repository.ApplicationUserRepository;
import ideas.ToDo_Application.todo_data.repository.CollaboratorsTaskRepository;
import ideas.ToDo_Application.todo_data.repository.TaskRepository;
import ideas.ToDo_Application.todo_data.service.CollaboratorsTaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CollaboratorsTaskRepositoryTest {

    @InjectMocks
    private CollaboratorsTaskService collaboratorsTaskService;

    @Mock
    private CollaboratorsTaskRepository collaboratorsTaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Test
    public void testDeleteByTask_TaskId() {
        // Arrange
        Integer taskId = 1;

        // Act
        collaboratorsTaskRepository.deleteByTask_TaskId(taskId);

        // Assert
        verify(collaboratorsTaskRepository, times(1)).deleteByTask_TaskId(taskId);
    }

    @Test
    public void testFindProjectionsByTask() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1);

        CollaboratorTaskProjection projection = Mockito.mock(CollaboratorTaskProjection.class);
        List<CollaboratorTaskProjection> projections = Collections.singletonList(projection);

        when(collaboratorsTaskRepository.findProjectionsByTask(task)).thenReturn(projections);

        // Act
        List<CollaboratorTaskProjection> result = collaboratorsTaskRepository.findProjectionsByTask(task);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(collaboratorsTaskRepository, times(1)).findProjectionsByTask(task);
    }

    @Test
    public void testFindByCollaboratorUser() {
        // Arrange
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);

        CollaboratorsTask collaboratorsTask = new CollaboratorsTask();
        List<CollaboratorsTask> collaboratorsTasks = Collections.singletonList(collaboratorsTask);

        when(collaboratorsTaskRepository.findByCollaboratorUser(user)).thenReturn(collaboratorsTasks);

        // Act
        List<CollaboratorsTask> result = collaboratorsTaskRepository.findByCollaboratorUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(collaboratorsTaskRepository, times(1)).findByCollaboratorUser(user);
    }

    @Test
    public void testFindByTask_TaskId() {
        // Arrange
        Integer taskId = 1;

        CollaboratorsTask collaboratorsTask = new CollaboratorsTask();
        List<CollaboratorsTask> collaboratorsTasks = Collections.singletonList(collaboratorsTask);

        when(collaboratorsTaskRepository.findByTask_TaskId(taskId)).thenReturn(collaboratorsTasks);

        // Act
        List<CollaboratorsTask> result = collaboratorsTaskRepository.findByTask_TaskId(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(collaboratorsTaskRepository, times(1)).findByTask_TaskId(taskId);
    }
}