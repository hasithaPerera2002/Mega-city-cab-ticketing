package org.icbt.hasitha.megacity.dto;

import org.icbt.hasitha.megacity.util.enums.RoleType;

import javax.management.relation.Role;

public class LoginResponseDTO {
    private boolean isValid;
    private String message;
    private String error;
    private RoleType role;
    private String username;

    public LoginResponseDTO(boolean isValid, String message, String error) {
        this.isValid = isValid;
        this.message = message;
        this.error = error;

    }

    public LoginResponseDTO(boolean isValid, String message, String error, RoleType role, String username) {
        this.isValid = isValid;
        this.message = message;
        this.error = error;
        this.role = role;
        this.username = username;
    }


    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }


    public void setError(String error) {
        this.error = error;
    }


    public RoleType getRole() {
        return role;
    }


    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "isValid=" + isValid +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", role=" + role +
                '}';
    }
}
