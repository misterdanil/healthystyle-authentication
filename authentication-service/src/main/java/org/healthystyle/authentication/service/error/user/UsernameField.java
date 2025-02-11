package org.healthystyle.authentication.service.error.user;

public class UsernameField extends Field<String> {

	public UsernameField(String value) {
		super(value);
	}

	@Override
	public String getName() {
		return "username";
	}
}
