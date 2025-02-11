package org.healthystyle.authentication.service.security.code;

import org.healthystyle.model.security.code.Algorithm;
import org.healthystyle.model.security.code.Type;

public interface AlgorithmService {
	Algorithm findByType(Type type);
}
