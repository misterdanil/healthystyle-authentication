package org.healthystyle.authentication.service.impl;

import org.healthystyle.authentication.repository.role.RoleRepository;
import org.healthystyle.authentication.service.role.RoleService;
import org.healthystyle.model.role.Name;
import org.healthystyle.model.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository repository;

	@Override
	public Role findByName(Name name) {
		return repository.findByName(name);
	}

}
