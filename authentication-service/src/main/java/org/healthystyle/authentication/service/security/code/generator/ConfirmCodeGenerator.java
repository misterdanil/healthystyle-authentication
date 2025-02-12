package org.healthystyle.authentication.service.security.code.generator;

import org.healthystyle.model.User;
import org.healthystyle.model.security.code.Algorithm;

public abstract class ConfirmCodeGenerator {
	private Algorithm algorithm;

	public ConfirmCodeGenerator(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public abstract String generate(User user);

	public Algorithm getAlgorithm() {
		return algorithm;
	}
}
