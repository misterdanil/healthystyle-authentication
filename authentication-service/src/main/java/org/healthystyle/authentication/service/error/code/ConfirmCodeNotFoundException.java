package org.healthystyle.authentication.service.error.code;

import org.healthystyle.authentication.service.error.AbstractException;
import org.healthystyle.authentication.service.error.Field;
import org.springframework.validation.BindingResult;

public class ConfirmCodeNotFoundException extends AbstractException {
	private Field<?> field;

	public ConfirmCodeNotFoundException(Field<?> field, BindingResult result) {
		super(result);
		this.field = field;
	}

	public Field<?> getField() {
		return field;
	}

	@Override
	public String getMessage() {
		return String.format("The confirm code was not found by field '%s' and value '%s'", field.getName(),
				field.getValue());
	}

}
