package org.healthystyle.authentication.service.error.user;

import org.healthystyle.authentication.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UserYoungException extends AbstractException {
	private int age;
	private int minAge;

	public UserYoungException(int age, int minAge, BindingResult result) {
		super(result);
		if (age >= minAge) {
			throw new IllegalArgumentException("Age is more or equal min age");
		}
		this.age = age;
		this.minAge = minAge;
	}

	public int getAge() {
		return age;
	}

	public int getMinAge() {
		return minAge;
	}

	@Override
	public String getMessage() {
		return String.format("A user is %s, but min age is %s", age, minAge);
	}

}
