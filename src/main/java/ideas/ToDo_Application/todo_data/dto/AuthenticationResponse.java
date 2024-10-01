package ideas.ToDo_Application.todo_data.model;

import ideas.ToDo_Application.todo_data.dto.UserLoginDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserLoginDTO userLoginDTO;
    private String jwt;
}
