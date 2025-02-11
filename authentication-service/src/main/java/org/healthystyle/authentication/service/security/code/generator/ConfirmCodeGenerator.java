package org.healthystyle.authentication.service.security.code.generator;

import org.healthystyle.model.User;
import org.healthystyle.model.security.code.Type;

public interface ConfirmCodeGenerator {
	boolean support(Type type);

	String generate(User user);
}
