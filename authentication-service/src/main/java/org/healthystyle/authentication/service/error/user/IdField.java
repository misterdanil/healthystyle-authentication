package org.healthystyle.authentication.service.error.user;

public class IdField extends Field<Long> {

	public IdField(Long value) {
		super(value);
	}

	@Override
	public String getName() {
		return "id";
	}
}
