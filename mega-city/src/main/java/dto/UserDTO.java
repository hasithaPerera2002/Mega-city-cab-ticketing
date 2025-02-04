package dto;

import util.enums.RoleType;

import java.util.UUID;

public class UserDTO {
    private UUID user_id;
    private String username;
    private String password_hash;
    private String email;
    private String phone_number;
    private String address;
    private RoleType role;
    private String nic;
    private String front_image;
    private String back_image;

}
