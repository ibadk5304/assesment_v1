package com.ibad.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserRegistrationRequest {
    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%\\.]).{8,}$", // source used for regex: https://saasbase.dev/tools/regex-generator
    message = "Password must needs to be greater than 8 charactar, contain at least 1 number, 1 capitalized letter, and 1 special character (e.g., # $ % .)")    
    private String password; 

    @NotBlank(message = "IP address must not be blank")
    private String ipAddress;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
