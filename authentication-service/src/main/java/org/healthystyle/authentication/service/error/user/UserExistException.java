package org.healthystyle.authentication.service.error.user;

import org.healthystyle.authentication.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UserExistException extends AbstractException {
	private Field<?> field;

	public UserExistException(Field<?> field, BindingResult result) {
		super(result);
		this.field = field;
	}

	@Override
	public String getMessage() {
		return String.format("A user exists by '%s' with value '%s'", field.getName(), field.getValue());
	}

}
