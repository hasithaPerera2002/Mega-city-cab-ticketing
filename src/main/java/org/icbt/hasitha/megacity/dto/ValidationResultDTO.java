package org.icbt.hasitha.megacity.dto;

public class ValidationResultDTO {
    private boolean isValid;
    private String message;
    private String error;

    public ValidationResultDTO(boolean isValid, String message, String error) {
        this.isValid = isValid;
        this.message = message;
        this.error = error;
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

    @Override
    public String toString() {
        return "ValidationResultDTO{" +
                "isValid=" + isValid +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
