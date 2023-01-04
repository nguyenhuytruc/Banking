package com.cg.model.dto;

import com.cg.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleDTO {

    @NotNull(message = "The role is required")
    private Long id;

    private String code;

    public Role toRole() {
        return new Role()
                .setId(id)
                .setCode(code);
    }

}
