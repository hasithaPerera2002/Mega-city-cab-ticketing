import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.controller.BookingController;
import org.icbt.hasitha.megacity.dto.ResultDTO;
import org.icbt.hasitha.megacity.dto.TripDetailsDTO;
import org.icbt.hasitha.megacity.service.BookingService;
import org.icbt.hasitha.megacity.util.JwtUtil;
import org.icbt.hasitha.megacity.util.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private final Gson gson = new Gson();
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;
    @Mock
    private SendResponse sendResponse;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingController = new BookingController(); // Inject the mock into the controller
    }

    @Test
    void testDoGetUnauthorized() throws IOException, ServletException {
        // Mock the behavior of the HttpServletRequest to return a null Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        bookingController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(anyString());

    }

    @Test
    void testDoGetValid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(request.getParameter("userEmail")).thenReturn("user@example.com");

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);

            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);
            bookingController.doGet(request, response);
            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoPostUnauthorized() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        bookingController.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(anyString());
    }

    @Test
    void testDoPostValid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");


        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);
            JwtUtil.isTokenExpired("validToken");

            TripDetailsDTO trip = new TripDetailsDTO();
            trip.setCustomerEmail("user@example.com");  // Ensure customerEmail is set
            trip.setVehicleType("SUV");
            trip.setDate(1625567889000L);  // Example timestamp
            trip.setDestination("Matara");
            trip.setStartTime("13:00");
            trip.setFare("150.00");
            trip.setDistance("50");
            trip.setStartLocation("Colommbo");

            trip.setPassengerCount(2);

            String tripJson = gson.toJson(trip);
            BufferedReader reader = new BufferedReader(new StringReader(tripJson));
            when(request.getReader()).thenReturn(reader);
            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);
            bookingController.doPost(request, response);
            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verify(writer).write(anyString());
        }
    }

    @Test
    void testDoPostInvalid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);

            // Create a TripDetailsDTO with missing or invalid fields
            TripDetailsDTO trip = new TripDetailsDTO();
            trip.setCustomerEmail(null);  // Missing customerEmail to trigger validation failure
            trip.setVehicleType("SUV");
            trip.setDate(1625567889000L);  // Example timestamp
            trip.setDestination("Matara");
            trip.setStartTime("13:00");
            trip.setFare("150.00");
            trip.setDistance("50");
            trip.setStartLocation("Colombo");
            trip.setPassengerCount(2);

            String tripJson = gson.toJson(trip);
            BufferedReader reader = new BufferedReader(new StringReader(tripJson));
            when(request.getReader()).thenReturn(reader);

            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);

            // Call the controller method
            bookingController.doPost(request, response);

            // Verify that a failure response is returned due to missing customerEmail
            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);  // Assuming bad request for missing email
            verify(writer).write(anyString());  // Check if error message was written
        }
    }


    @Test
    void testDoDeleteUnauthorized() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        bookingController.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write(anyString());
    }

    @Test
    void testDoDeleteValid() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.isTokenExpired("validToken")).thenReturn(false);

            when(request.getParameter("id")).thenReturn("0d0fd667-7ce2-4d4f-9a0d-258c3579a9e6");
            doReturn(false).when(JwtUtil.class);
            JwtUtil.isTokenExpired("validToken");
            PrintWriter writer = mock(PrintWriter.class);
            when(response.getWriter()).thenReturn(writer);
            bookingController.doDelete(request, response);
            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(response.getWriter()).write(anyString());
        }
    }

}
