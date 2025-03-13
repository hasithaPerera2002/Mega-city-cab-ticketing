package org.icbt.hasitha.megacity.repository.interfaces;

import org.icbt.hasitha.megacity.dto.SignUpDTO;
import org.icbt.hasitha.megacity.entity.User;

import java.sql.SQLException;

public interface IUserRepo extends ISuperRepo{
    User findUserByEmail(String email) throws SQLException;
    boolean saveUser(SignUpDTO signUpDTO);
    boolean checkUserExists(String email);
}
