package org.healthystyle.authentication.service.security.code.impl;

import java.time.Instant;
import java.util.LinkedHashMap;

import org.healthystyle.authentication.repository.security.confirmcode.ConfirmCodeRepository;
import org.healthystyle.authentication.service.error.ValidationException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class ConfirmCodeServiceImpl implements ConfirmCodeService {
	@Autowired
	private ConfirmCodeRepository repository;
	@Autowired
	private ConfirmCodeGenerator generator;
	private static final int MAX_SIZE = 50;

	private static final Logger LOG = LoggerFactory.getLogger(ConfirmCodeServiceImpl.class);

	@Override
	public ConfirmCode findByToken(String token) {
		Assert.notNull(token, "Token must be not null");

		LOG.debug("Getting confirm code by token '{}'", token);
		ConfirmCode code = repository.findByToken(token);
		LOG.info("Got confirm code by token '{}'", token);

		return code;
	}

	@Override
	public ConfirmCode findByUser(long userId) {
		LOG.debug("Getting confirm code by user id '{}'", userId);
		ConfirmCode code = repository.findByUser(userId);
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

	public ConfirmCode generate(User user) {
		Assert.notNull(user, "User must be not null");

		LOG.debug("Generating a confirm code for the user: {}", user.getId());
		
		String token = generator.generate(user);
		ConfirmCode code = new ConfirmCode(null, null, user, null, null)
	}

	@Override
	public void confirm(String token) {
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

}
