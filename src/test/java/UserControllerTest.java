
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.controller.UserController;
import org.icbt.hasitha.megacity.dto.*;
import org.icbt.hasitha.megacity.service.UserService;
import org.icbt.hasitha.megacity.util.SendResponse;
import org.icbt.hasitha.megacity.util.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SendResponse sendResponse;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    private UserController userController;

    private Gson gson = new Gson();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController();
    }

    @Test
    public void testLoginUser_Success() throws IOException, ServletException {
        // Prepare input
        LoginDTO loginDTO = new LoginDTO("user@example.com", "asfafW123");
        StringReader stringReader = new StringReader(gson.toJson(loginDTO));
        when(request.getReader()).thenReturn(new BufferedReader(stringReader));

        // Mock path
        when(request.getPathInfo()).thenReturn("/login");

        // Mock valid user response
        LoginResponseDTO mockResponse = new LoginResponseDTO(true, "Login successful", "", RoleType.USER, "user@example.com");

        when(userService.validateUser(anyString(), anyString())).thenReturn(mockResponse);

        // Mock response writer
        when(response.getWriter()).thenReturn(writer);

        userController.doPost(request, response);

        // Verify response
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setHeader(eq("Authorization"), contains("Bearer"));
        verify(writer).write(anyString());
    }

    @Test
    public void testLoginUser_Failure() throws IOException {
        LoginDTO loginDTO = new LoginDTO("testuser@example.com", "wrongpassword");
        StringReader stringReader = new StringReader(gson.toJson(loginDTO));
        when(request.getReader()).thenReturn(new BufferedReader(stringReader));

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(false,"testuser", "user_role" );
        when(userService.validateUser(anyString(), anyString())).thenReturn(loginResponseDTO);

        when(response.getWriter()).thenReturn(writer);

        userController.loginUser(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(anyString());
    }

    @Test
    public void testRegisterUser_Success() throws IOException {
        SignUpDTO signUpDTO = new SignUpDTO("newuser", "Password123", "newuser2@example.com", "1234567890", "Some address");
        StringReader stringReader = new StringReader(gson.toJson(signUpDTO));
        when(request.getReader()).thenReturn(new BufferedReader(stringReader));

        when(userService.isExistingUser(anyString())).thenReturn(false);
        when(userService.registerUser(any())).thenReturn(true);
        when(response.getWriter()).thenReturn(writer);

        userController.registerUser(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(anyString());
    }

    @Test
    public void testRegisterUser_Failure() throws IOException {
        SignUpDTO signUpDTO = new SignUpDTO("newuser", "password123", "newuser@example.com", "1234567890", "Some address");
        StringReader stringReader = new StringReader(gson.toJson(signUpDTO));
        when(request.getReader()).thenReturn(new BufferedReader(stringReader));

        when(userService.isExistingUser(anyString())).thenReturn(true);
        when(response.getWriter()).thenReturn(writer);

        userController.registerUser(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(anyString());
    }
}
