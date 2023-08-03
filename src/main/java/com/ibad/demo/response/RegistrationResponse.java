package com.ibad.demo.response;

public class RegistrationResponse {
    private String yuid;
    private String message;

    public RegistrationResponse(String yuid, String message) {
        this.yuid = yuid;
        this.message = message;
    }

    public String getYuid() {
        return yuid;
    }

    public void setYuid(String yuid) {
        this.yuid = yuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
