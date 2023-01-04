package com.cg.service.role;

import com.cg.model.Role;
import com.cg.service.IGeneralService;

public interface IRoleService extends IGeneralService<Role> {
    Role findByName(String name);
}
