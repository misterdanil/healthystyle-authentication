package org.healthystyle.authentication.service.security.code.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;

import org.healthystyle.authentication.repository.security.confirmcode.ConfirmCodeRepository;
import org.healthystyle.authentication.service.UserService;
import org.healthystyle.authentication.service.error.ValidationException;
import org.healthystyle.authentication.service.error.code.ConfirmCodeNotFoundException;
import org.healthystyle.authentication.service.error.code.UserIdField;
import org.healthystyle.authentication.service.error.token.TokenField;
import org.healthystyle.authentication.service.error.user.UserNotFoundException;
import org.healthystyle.authentication.service.log.LogTemplate;
import org.healthystyle.authentication.service.security.code.ConfirmCodeService;
import org.healthystyle.authentication.service.security.code.generator.ConfirmCodeGenerator;
import org.healthystyle.authentication.service.validation.ParamsChecker;
import org.healthystyle.model.User;
import org.healthystyle.model.security.code.ConfirmCode;
import org.healthystyle.model.security.code.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
@PropertySource(value = "classpath:/confirm_code.properties")
public class ConfirmCodeServiceImpl implements ConfirmCodeService {
	@Autowired
	private ConfirmCodeRepository repository;
	@Autowired
	private ConfirmCodeGenerator generator;
	@Autowired
	private Environment env;
	@Autowired
	private UserService userService;
	private final long EXPIRES_MILLIS = 300000L;

	private static final int MAX_SIZE = 50;

	private static final Logger LOG = LoggerFactory.getLogger(ConfirmCodeServiceImpl.class);

	@Override
	public ConfirmCode findByToken(String token) throws ConfirmCodeNotFoundException {
		Assert.notNull(token, "Token must be not null");

		LOG.debug("Getting confirm code by token '{}'", token);
		ConfirmCode code = repository.findByToken(token);

		if (code == null) {
			LOG.warn("There is no confirm code by token '{}'", token);

			BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "confirmCode");
			result.reject("confirm_code.find.token.notExist", "Кода доступа с таким значением не существует");
			throw new ConfirmCodeNotFoundException(new TokenField(token), result);
		}

		LOG.info("Got confirm code by token '{}'", token);

		return code;
	}

	@Override
	public ConfirmCode findByUser(long userId) throws ConfirmCodeNotFoundException {
		LOG.debug("Getting confirm code by user id '{}'", userId);
		ConfirmCode code = repository.findByUser(userId);
		if (code == null) {
			LOG.warn("There is no confirm code by user id '{}'", userId);

			BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "confirmCode");
			result.reject("confirm_code.find.user_id.notExist",
					"Не удалось найти код подтверждения для данного пользователя");
			throw new ConfirmCodeNotFoundException(new UserIdField(userId), result);
		}

		LOG.info("Got confirm code by user id '{}'", userId);

		return code;
	}

	@Override
	public Page<ConfirmCode> findByAlgorithm(Type type, int page, int limit) throws ValidationException {
		LOG.debug("Getting confirm codes by type '{}'", type);

		LOG.info("Validating params: {}, {}, {}", type, page, limit);
		BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "confirmCode");

		if (type == null) {
			LOG.warn("Type must be not null");
			result.rejectValue("type", "Type must be not null");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			LOG.warn("The params are invalid: {}", result);
			throw new ValidationException(
					"Exception occurred while fetching confirm codes by algorithm '%s'. Params are invalid: %s", result,
					type, result);
		}

		LOG.debug("The params are OK: {}, {}, {}", type, page, limit);

		Page<ConfirmCode> codes = repository.findByAlgorithm(type, PageRequest.of(page, limit));
		LOG.info("Got confirm codes by type '{}', page '{}' and limit '{}'", type, page, limit);

		return codes;
	}

	@Override
	public Page<ConfirmCode> findByTime(Instant issuedAt, Instant expiredAt, int page, int limit)
			throws ValidationException {
		Object[] params = new Object[] { issuedAt, expiredAt, page, limit };
		String[] paramNames = new String[] { "issued at", "expired at", "page", "limit" };

		String logTemplate = LogTemplate.getParamsTemplate("{}", paramNames);
		LOG.debug("Getting confirm codes by " + logTemplate, params);

		LOG.info("Validating params: " + logTemplate, params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "confirmCode");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		ParamsChecker.checkDates(issuedAt, expiredAt, result);

		if (result.hasErrors()) {
			LOG.warn("The params are invalid: {}", result);
			throw new ValidationException("Exception occurred while fetching confirm codes by "
					+ LogTemplate.getParamsTemplate("%s", paramNames), result, params);
		}

		LOG.info("The params are OK: " + logTemplate, params);

		Page<ConfirmCode> codes = repository.findByTime(issuedAt, expiredAt, PageRequest.of(page, limit));
		LOG.info("Got confirm codes by " + logTemplate, params);

		return codes;
	}

	public ConfirmCode save(User user) {
		deleteByUser(user.getId());

		ConfirmCode code = generate(user);

		code = save(code);

		return code;
	}

	private ConfirmCode generate(User user) {
		LOG.debug("Generating a confirm code for the user: {}", user.getId());
		String token = generator.generate(user);
		Instant issuedAt = Instant.now();
		Instant expiredAt = issuedAt.plus(EXPIRES_MILLIS, ChronoUnit.MILLIS);

		ConfirmCode code = new ConfirmCode(token, generator.getAlgorithm(), user, issuedAt, expiredAt);
		LOG.info("The code for user '{}' was generated", user.getId());
		return code;
	}

	private ConfirmCode save(ConfirmCode code) {
		code = repository.save(code);
		LOG.info("Confirm code was saved for user '{}'", code.getUser().getId());
		return code;
	}

	@Override
	public ConfirmCode save() {
		Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
		User user;
		try {
			user = userService.findById(userId);
		} catch (UserNotFoundException e) {
			LOG.error("Could find the user by user id '{}', but got it from security context", userId, e);
			throw new RuntimeException(
					"Exception occurred while saving a confirm code. User with id '" + userId + "' doesn't exist", e);
		}

		deleteByUser(userId);

		ConfirmCode code = generate(user);
		code = save(code);

		return code;

	}

	@Override
	public void confirm(String token) throws ConfirmCodeNotFoundException {
		LOG.debug("Confirming a user by token: {}", token);

		ConfirmCode code = findByToken(token);
		code.setConfirmed(true);
		repository.save(code);
		LOG.info("The code '{}' was confirmed", code);
	}

	@Override
	public void deleteByUser(long userId) {
		LOG.debug("Deleting a confirm code by user id: {}", userId);
		repository.deleteByUser(userId);
		LOG.info("The confirm code by user id '{}' was deleted", userId);
	}

	@Override
	public boolean existsByToken(String token) {
		Assert.notNull(token, "Token must be not null");

		LOG.debug("Checking for the existence");
		boolean exists = repository.existsByToken(token);
		LOG.info("Got existing info: {}", exists);

		return exists;
	}

	@Scheduled(fixedRate = EXPIRES_MILLIS)
	public void deleteExpiredAndNotConfirmed() {
		LOG.debug("Deleting expired and not confirmed confirm codes");
		repository.deleteExpiredAndNotConfirmed();
	}

}
