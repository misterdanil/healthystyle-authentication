package org.healthystyle.authentication.service.dto;

import java.time.LocalDate;

import org.healthystyle.model.constraints.regexp.UserRegexp;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public class UserSaveRequest {
	@NotEmpty
	@Pattern(regexp = UserRegexp.USERNAME, message = "Минимальное количество символов для имени: 3")
	private String username;
	@NotEmpty
	@Pattern(regexp = UserRegexp.EMAIL, message = "Электронная почта должна быть формата: test@mail.ru")
	private String email;
	@NotEmpty
	@Pattern(regexp = UserRegexp.PASSWORD, message = "Пароль должен содержать как минимум один большой и маленький символы, цифру и специальный символ @$!%*?&_")
	private String password;
	@Past(message = "Дата рождения должна быть прошлого времени")
	private LocalDate birthDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "UserSaveRequest [username=" + username + ", email=" + email + ", birthDate=" + birthDate + "]";
	}

}
