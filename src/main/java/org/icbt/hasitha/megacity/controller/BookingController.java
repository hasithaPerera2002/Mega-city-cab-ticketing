package org.icbt.hasitha.megacity.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.icbt.hasitha.megacity.service.BookingService;
import org.icbt.hasitha.megacity.util.JwtUtil;

import java.io.IOException;
@WebServlet(urlPatterns = "/api/booking/*", name = "BookingController")
public class BookingController extends HttpServlet {


    private final BookingService bookingService;

    public BookingController() {
        this.bookingService = new BookingService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("Authorization");

        if (token == null || token.trim().isEmpty()) {
            sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Authorization token is required");
            return;
        }

        token = token.trim();
        boolean isValid = JwtUtil.isTokenValidAndNotExpired(token);

        if (!isValid) {
           sendJsonResponse( resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Unauthorized access", "Invalid token");
        }




    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    private void sendJsonResponse(HttpServletResponse resp, int statusCode, String statusKey, String message, String description) throws IOException {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty(statusKey, message);
        if (description != null) {
            responseJson.addProperty("description", description);
            responseJson.addProperty("message", message);
        }

        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.getWriter().write(responseJson.toString());
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
