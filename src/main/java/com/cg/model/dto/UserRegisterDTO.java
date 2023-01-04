package com.cg.model.dto;

import com.cg.model.User;
import lombok.*;
import lombok.experimental.Accessors;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserRegisterDTO {

    private Long id;

    @NotBlank(message = "The email is required")
    @Email(message = "The email address is invalid")
    @Size(max = 50, message = "The length of email must be between 5 and 50 characters")
    private String username;

    @NotBlank(message = "The password is required")
    @Size(max = 30, message = "Maximum password length 30 characters")
    private String password;

    @Valid
    private RoleDTO role;

    public UserRegisterDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public User toUser() {
        return new User()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setRole(role.toRole());
    }

}
