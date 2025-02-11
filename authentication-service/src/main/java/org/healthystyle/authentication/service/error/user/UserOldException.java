package org.healthystyle.authentication.service.error.user;

import org.healthystyle.authentication.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UserOldException extends AbstractException {
	private int age;
	private int maxAge;

	public UserOldException(int age, int maxAge, BindingResult result) {
		super(result);
		if (age <= maxAge) {
			throw new IllegalArgumentException(String.format("Age '%s' is less or equal max age '%s'", age, maxAge));
		}
		this.age = age;
		this.maxAge = maxAge;
	}

	public int getAge() {
		return age;
	}

	public int getMaxAge() {
		return maxAge;
	}

	@Override
	public String getMessage() {
		return String.format("A user is %s, but max age is %s", age, maxAge);
	}

}
