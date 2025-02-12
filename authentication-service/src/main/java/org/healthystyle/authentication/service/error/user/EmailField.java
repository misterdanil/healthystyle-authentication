package org.healthystyle.authentication.service.error.user;

import org.healthystyle.authentication.service.error.Field;

public class EmailField extends Field<String> {

	public EmailField(String value) {
		super(value);
	}

	@Override
	public String getName() {
		return "email";
	}
}
