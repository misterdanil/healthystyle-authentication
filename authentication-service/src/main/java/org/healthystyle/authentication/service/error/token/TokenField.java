package org.healthystyle.authentication.service.error.token;

import org.healthystyle.authentication.service.error.Field;

public class TokenField extends Field<String> {

	public TokenField(String value) {
		super(value);
	}

	@Override
	public String getName() {
		return "token";
	}

}
