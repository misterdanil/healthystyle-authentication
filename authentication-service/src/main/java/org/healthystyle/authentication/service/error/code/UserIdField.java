package org.healthystyle.authentication.service.error.code;

import org.healthystyle.authentication.service.error.Field;

public class UserIdField extends Field<Long> {

	public UserIdField(Long value) {
		super(value);
	}

	@Override
	public String getName() {
		return "userId";
	}

}
