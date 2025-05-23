package org.healthystyle.authentication.service.error;

import org.springframework.validation.BindingResult;

public class ValidationException extends AbstractException {
	public ValidationException(String message, BindingResult result, Object... args) {
		super(message, result, args);
	}
}
