package entity;

import java.util.UUID;

public class User {
    private UUID user_id;
    private String username;
    private String password_hash;
    private String email;
    private String phone_number;
    private String address;
    private String role;
    private String nic;
    private String front_image;
    private String back_image;

    public User() {
    }
    public User(UUID user_id, String username, String password_hash, String email, String phone_number, String address, String role, String nic, String front_image, String back_image, String status) {
        this.user_id = user_id;
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.role = role;
        this.nic = nic;
        this.front_image = front_image;
        this.back_image = back_image;
    }
    public String getBack_image() {
        return back_image;
    }

    public void setBack_image(String back_image) {
        this.back_image = back_image;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getFront_image() {
        return front_image;
    }

    public void setFront_image(String front_image) {
        this.front_image = front_image;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", nic='" + nic + '\'' +
                ", front_image='" + front_image + '\'' +
                ", back_image='" + back_image + '\'' +
                '}';
    }
}