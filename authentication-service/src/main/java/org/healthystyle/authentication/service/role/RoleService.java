package org.healthystyle.authentication.service.role;

import org.healthystyle.model.role.Name;
import org.healthystyle.model.role.Role;

public interface RoleService {
	Role findByName(Name name);
}
