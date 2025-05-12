package org.healthystyle.authentication.service.error;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class AbstractException extends Exception {
	private List<String> globalErrors;
	private Map<String, String> fieldErrors;

	public AbstractException(String message, BindingResult result, Object... args) {
		super(String.format(message, args));
		globalErrors = getGlobalErrorsValues(result.getGlobalErrors());
		fieldErrors = getFieldErrorsValues(result.getFieldErrors());
	}

	public AbstractException(BindingResult result) {
		super();
		globalErrors = getGlobalErrorsValues(result.getGlobalErrors());
		fieldErrors = getFieldErrorsValues(result.getFieldErrors());
	}

	private List<String> getGlobalErrorsValues(List<ObjectError> globalErrors) {
		List<String> globalErrorsValues = new ArrayList<>();
		globalErrors.forEach(ge -> globalErrorsValues.add(ge.getDefaultMessage()));

		return globalErrorsValues;
	}

	private Map<String, String> getFieldErrorsValues(List<FieldError> fieldErrors) {
		Map<String, String> fieldErrorsValues = new LinkedHashMap<>();
		fieldErrors.forEach(fe -> fieldErrorsValues.put(fe.getField(), fe.getDefaultMessage()));

		return fieldErrorsValues;
	}

	public List<String> getGlobalErrors() {
		return globalErrors;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
