package org.icbt.hasitha.megacity.dto;


import org.icbt.hasitha.megacity.util.enums.RoleType;

import java.util.UUID;

public class UserDTO {
    private UUID user_id;
    private String username;
    private String password_hash;
    private String email;
    private String phone_number;
    private String address;
    private RoleType role;


    @Override
    public String toString() {
        return "UserDTO{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role +
                '}';
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }


    public UserDTO() {
    }

    public UserDTO(UUID user_id, String username, String password_hash, String email, String phone_number, String address, RoleType role) {
        this.user_id = user_id;
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.role = role;

    }



}
