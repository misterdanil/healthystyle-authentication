package org.healthystyle.authentication.service.error.user;

import org.healthystyle.authentication.service.error.Field;

public class TelephoneNumberField extends Field<String> {

	public TelephoneNumberField(String value) {
		super(value);
	}

	@Override
	public String getName() {
		return "telephone number";
	}
}
