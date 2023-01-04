package com.cg.service.user;

import com.cg.model.User;
import com.cg.model.dto.UserRegisterDTO;
import com.cg.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User>, UserDetailsService {
    User getByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<UserRegisterDTO> findUserDTOByUsername(String username);

    Boolean existsByUsername(String email);
}
