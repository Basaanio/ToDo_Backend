import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ideas.ToDo_Application.todo_data.dto.ApplicationUserDTO;
import ideas.ToDo_Application.todo_data.entity.ApplicationUser;
import ideas.ToDo_Application.todo_data.repository.ApplicationUserRepository;
import ideas.ToDo_Application.todo_data.service.ApplicationUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class ApplicationUserServiceTest {

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @InjectMocks
    private ApplicationUserService applicationUserService; // Ensure this matches your actual service class name

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers_pass() {
        // Arrange
        ApplicationUser user1 = new ApplicationUser(1, "user1", "user1@example.com", "password1", null, null, "USER");
        ApplicationUser user2 = new ApplicationUser(2, "user2", "user2@example.com", "password2", null, null, "USER");
        when(applicationUserRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<ApplicationUserDTO> result = applicationUserService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(applicationUserRepository).findAll();
    }

    @Test
    public void testGetAllUsers_fail() {
        when(applicationUserRepository.findAll()).thenReturn(Arrays.asList()); // No users
        List<ApplicationUserDTO> result = applicationUserService.getAllUsers();
        assertNotEquals(2, result.size()); // This should fail
    }
}
