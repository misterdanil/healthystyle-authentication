package org.healthystyle.authentication.service.security.code.impl;

import org.healthystyle.authentication.repository.security.confirmcode.AlgorithmRepository;
import org.healthystyle.authentication.service.security.code.AlgorithmService;
import org.healthystyle.model.security.code.Algorithm;
import org.healthystyle.model.security.code.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {
	@Autowired
	private AlgorithmRepository repository;

	@Override
	public Algorithm findByType(Type type) {
		Assert.notNull(type, "Type must be not null");
		return repository.findByType(type);
	}

}
