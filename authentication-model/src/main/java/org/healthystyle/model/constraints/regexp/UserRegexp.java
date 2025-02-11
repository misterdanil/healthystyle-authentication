package org.healthystyle.model.constraints.regexp;

public class UserRegexp {
	private UserRegexp() {
	}

	public static final String USERNAME = "\\w{3,}";
	public static final String EMAIL = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$";
}
