package org.icbt.hasitha.megacity.service.interfaces;

import org.icbt.hasitha.megacity.dto.LoginResponseDTO;
import org.icbt.hasitha.megacity.dto.SignUpDTO;

public interface IUserService extends ISuperService{
    LoginResponseDTO validateUser(String email, String password);
    boolean registerUser(SignUpDTO signUpDTO);
}
