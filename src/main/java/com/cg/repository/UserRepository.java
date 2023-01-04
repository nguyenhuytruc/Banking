package com.cg.repository;

import com.cg.model.User;
import com.cg.model.dto.UserRegisterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getByUsername(String username);

    Optional<User> findByUsername(String username);


    @Query("SELECT NEW com.cg.model.dto.UserRegisterDTO (" +
            "u.id, " +
            "u.username" +
            ") " +
            "FROM User u " +
            "WHERE u.username = ?1"
    )
    Optional<UserRegisterDTO> findUserDTOByUsername(String username);


    Boolean existsByUsername(String username);
}
