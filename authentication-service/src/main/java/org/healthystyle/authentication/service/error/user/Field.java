package org.healthystyle.authentication.service.error.user;

abstract class Field<T> {
	private String name;
	private T value;

	public Field(T value) {
		super();
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}
}
