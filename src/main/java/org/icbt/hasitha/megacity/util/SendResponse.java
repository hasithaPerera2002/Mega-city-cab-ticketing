package org.icbt.hasitha.megacity.util;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SendResponse {


    public void SendJsonResponse(HttpServletResponse resp, int statusCode, String statusKey, String message, String description) throws IOException {
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
