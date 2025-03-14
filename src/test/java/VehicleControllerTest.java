import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.controller.VehicleController;
import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.VehicleDTO;
import org.icbt.hasitha.megacity.service.VehicleService;
import org.icbt.hasitha.megacity.util.JwtUtil;
import org.icbt.hasitha.megacity.util.SendResponse;
import org.icbt.hasitha.megacity.util.enums.VehicleStatus;
import org.icbt.hasitha.megacity.util.enums.VehicleTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;
    @Mock
    private SendResponse sendResponse;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private VehicleController vehicleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleController = new VehicleController();

    }

    @Test
    void testDoPostUnauthorized() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        vehicleController.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(anyString());
    }

    @Test
    void testDoPostValid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);
            mockedJwtUtil.when(() -> JwtUtil.isAdmin("validToken")).thenReturn(true);
            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setVehicle_number("ABC123");
            vehicleDTO.setDriver_name("John Doe");
            vehicleDTO.setVehicle_type(VehicleTypes.valueOf("SUV"));

            String vehicleJson = new Gson().toJson(vehicleDTO);
            BufferedReader reader = new BufferedReader(new StringReader(vehicleJson));
            when(request.getReader()).thenReturn(reader);

            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);

            ResultDTO<Boolean> resultDTO = new ResultDTO<>("Success", 500, true);
            when(vehicleService.addVehicle(any(VehicleDTO.class))).thenReturn(resultDTO);

            vehicleController.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoPostInvalid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);
            mockedJwtUtil.when(() -> JwtUtil.isAdmin("validToken")).thenReturn(true);

            // Initialize the VehicleDTO with required fields
            VehicleDTO vehicleDTO = new VehicleDTO(UUID.randomUUID(), "", VehicleTypes.SUV, VehicleStatus.AVAILABLE, null, "1234567890123", "0712456884 ", "5000908098", "kamaaal@gmail.com");
            String vehicleJson = new Gson().toJson(vehicleDTO);
            BufferedReader reader = new BufferedReader(new StringReader(vehicleJson));
            when(request.getReader()).thenReturn(reader);
            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);
            vehicleController.doPost(request, response);
            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoGetUnauthorized() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);
            mockedJwtUtil.when(() -> JwtUtil.isAdmin("validToken")).thenReturn(false);

            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);

            vehicleController.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoGetValid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);

            VehicleDTO[] vehicles = new VehicleDTO[]{new VehicleDTO()}; // Mock response data
            when(vehicleService.getVehicles()).thenReturn(vehicles);

            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);

            vehicleController.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoPutUnauthorized() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);

            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);

            vehicleController.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoPutValid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);
            mockedJwtUtil.when(() -> JwtUtil.isAdmin("validToken")).thenReturn(true);

            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setVehicle_id(UUID.fromString("96734cb7-c3a7-48cf-92e3-eb990519c19f"));
            vehicleDTO.setVehicle_number("XYZ123");
            vehicleDTO.setDriver_name("Jane Doe");
            String vehicleJson = new Gson().toJson(vehicleDTO);
            BufferedReader reader = new BufferedReader(new StringReader(vehicleJson));
            when(request.getReader()).thenReturn(reader);
            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);
            ResultDTO<Boolean> resultDTO = new ResultDTO<>("Vehicle updated successfully", 200, true);
            vehicleController.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoDeleteUnauthorized() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        vehicleController.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(anyString());
    }

    @Test
    void testDoDeleteValid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);
            mockedJwtUtil.when(() -> JwtUtil.isAdmin("validToken")).thenReturn(true);

            when(request.getParameter("vehicleId")).thenReturn("d9f59a36-4781-46ac-9600-d1e8f2a0a8d5");
            ResultDTO<Boolean> resultDTO = new ResultDTO<>("Vehicle deleted successfully", HttpServletResponse.SC_OK, true);
            when(vehicleService.deleteVehicle(any(UUID.class))).thenReturn(resultDTO);

            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);

            vehicleController.doDelete(request, response);

            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(writer).write(anyString());
        }
    }
}
