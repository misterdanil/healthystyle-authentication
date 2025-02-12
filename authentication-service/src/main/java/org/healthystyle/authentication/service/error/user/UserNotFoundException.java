package org.healthystyle.authentication.service.error.user;

import org.healthystyle.authentication.service.error.AbstractException;
import org.healthystyle.authentication.service.error.Field;
import org.springframework.validation.BindingResult;

public class UserNotFoundException extends AbstractException {
	private Field<?> field;

	public UserNotFoundException(Field<?> field, BindingResult result) {
		super(result);
	}

	public Field<?> getField() {
		return field;
	}

	@Override
	public String getMessage() {
		return String.format("A user with field '%s' was not found by '%s' value", field.getName(), field.getValue());
	}

}
