package org.healthystyle.authentication.service.log;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LogTemplate {
	private LogTemplate() {
	}

	public static String getParamsTemplate(String specifier, String... paramNames) {
		return String.join(", ", Arrays.asList(paramNames).stream().map(param -> param + " '" + specifier + "'")
				.collect(Collectors.toList()).toArray(new String[paramNames.length])).trim();
	}
	
	public static void main(String[] args) {
		System.out.println(getParamsTemplate("%s", "name", "role", "page"));
	}
}
