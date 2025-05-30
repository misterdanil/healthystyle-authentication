package org.healthystyle.authentication.service.validation;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

public class ParamsChecker {
	private static final Logger LOG = LoggerFactory.getLogger(ParamsChecker.class);

	private ParamsChecker() {
	}

	public static void checkPageNumber(int pageNumber, BindingResult result) {
		LOG.debug("Checking page number");

		if (pageNumber < 0) {
			LOG.warn("Page number is zero or negative: {}", pageNumber);
			result.reject("*.*.pageNumber.zero_or_negative", "Номер страницы должен быть больше нуля");
		}
	}

	public static void checkLimit(int limit, final int MAX_SIZE, BindingResult result) {
		LOG.debug("Checking a limit '{}'", limit);

		if (limit < 1) {
			LOG.warn("The limit '{}' is less than 1. Returning 1", limit);
			result.reject("*.*.limit.zero_or_negative", "Лимит должен быть больше нуля");
		}
		if (limit > MAX_SIZE) {
			LOG.warn("The limit '{}' is more than max size '{}'. Returning max size", limit, MAX_SIZE);
			result.reject("*.*.limit.max_size",
					String.format("Лимит больше максимально допустимого пакета данных (%s)", MAX_SIZE));
		}
	}

	public static void checkDates(Instant fromDate, Instant toDate, BindingResult result) {
		LOG.debug("Checking from and to dates: {}, {}", fromDate, toDate);

		if (fromDate.isAfter(toDate)) {
			LOG.warn("From date '{}' is before to date '{}'", fromDate, toDate);
			result.reject("*.*.date.mixed", "Дата начала должны быть раньше даты конца");
		}
	}
}
