package org.healthystyle.authentication.service.impl;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.healthystyle.authentication.repository.UserRepository;
import org.healthystyle.authentication.service.UserService;
import org.healthystyle.authentication.service.confidentiality.ConfidentialityService;
import org.healthystyle.authentication.service.dto.UserSaveRequest;
import org.healthystyle.authentication.service.error.ValidationException;
import org.healthystyle.authentication.service.error.code.UserIdField;
import org.healthystyle.authentication.service.error.user.EmailField;
import org.healthystyle.authentication.service.error.user.TelephoneNumberField;
import org.healthystyle.authentication.service.error.user.UserExistException;
import org.healthystyle.authentication.service.error.user.UserNotFoundException;
import org.healthystyle.authentication.service.error.user.UserOldException;
import org.healthystyle.authentication.service.error.user.UserYoungException;
import org.healthystyle.authentication.service.error.user.UsernameField;
import org.healthystyle.authentication.service.log.LogTemplate;
import org.healthystyle.authentication.service.role.RoleService;
import org.healthystyle.authentication.service.security.code.ConfirmCodeService;
import org.healthystyle.authentication.service.validation.ParamsChecker;
import org.healthystyle.model.User;
import org.healthystyle.model.confidentiality.Confidentiality;
import org.healthystyle.model.role.Name;
import org.healthystyle.model.role.Role;
import org.healthystyle.model.security.code.ConfirmCode;
import org.healthystyle.model.sex.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private Validator validator;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ConfirmCodeService codeService;
	@Autowired
	private ConfidentialityService confidentialityService;
	public static final Integer MAX_SIZE = 50;

	private static final Integer MIN_AGE = 18;
	private static final Integer MAX_AGE = 150;

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User findById(long id) throws UserNotFoundException {
		LOG.debug("Fetching a user by id: {}", id);
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			LOG.warn("There is no the user with id '{}'", id);
			BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "user");
			result.reject("user.find.id.notExist", "Пользователя с таким идентификатором не существует");
			throw new UserNotFoundException(new UserIdField(id), result);
		}

		LOG.info("Found user by id '{}'", id);
		return user.get();
	}

	@Override
	public Page<User> findByIds(List<Long> ids) {
		LOG.info("Getting users by ids: {}", ids);
		if (ids == null || ids.isEmpty()) {
			LOG.warn("Ids is null or empty");
			return Page.empty();
		}

		Page<User> users = userRepository.findByIds(ids);
		LOG.info("Got users by ids: {}", ids);
		return users;
	}

	@Override
	public User findByUsername(String username) throws UserNotFoundException {
		LOG.debug("Getting a user by username '{}'", username);

		User user = userRepository.findByUsername(username);
		if (user == null) {
			LOG.warn("The user with username '{}' was not found", username);
			BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "user");
			result.rejectValue("username", "user.find.username.notExist", "Пользователя с данным именем не существует");
			throw new UserNotFoundException(new UsernameField(username), result);
		}

		LOG.info("The user with username '{}' was found");
		return user;
	}

	@Override
	public Page<User> findByUsernameAndName(String name, int page, int limit, Sort sort) throws ValidationException {
		LOG.debug("Start fetching users by name '{}'", name);

		BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "user");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			LOG.warn("The passed data have validation errors: {}", result);
			throw new ValidationException(String.format("The passed data are invalid: %s", result), result);
		}

		LOG.debug("The data are OK");

		Page<User> users = userRepository.findByUsernameAndName(name, PageRequest.of(page, limit),
				Sort.by(Direction.ASC, "username"));
		LOG.info("Got users by name '{}' with page '{}' and limit '{}'", name, page, limit);

		return users;
	}

	@Override
	public User findByTelephoneNumber(String telephoneNumber) throws UserNotFoundException {
		LOG.debug("Start fetching a user by telephone number: {}", telephoneNumber);

		User user = userRepository.findByTelephoneNumber(telephoneNumber);
		if (user == null) {
			LOG.warn("The user with telephone number '{}' doesn't exist", telephoneNumber);
			BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "user");
			result.rejectValue("telephoneNumber", "user.find.telphone_number.notExist",
					"Пользователя с таким номером телефона не существует");
			throw new UserNotFoundException(new TelephoneNumberField(telephoneNumber), result);
		}

		LOG.info("Got the user with telephone number: {}", telephoneNumber);
		return user;
	}

	@Override
	public User findByEmail(String email) throws UserNotFoundException {
		LOG.debug("Start fetching a user with email: {}", email);

		User user = userRepository.findByEmail(email);
		if (user == null) {
			LOG.warn("The user by email '{}' doesn't exist");
			BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "user");
			result.rejectValue("email", "user.find.email.notExist", "Пользователя с такой почтой не существует");
			throw new UserNotFoundException(new EmailField(email), result);
		}

		LOG.info("The user with email '{}' was fetched successfully", email);

		return user;
	}

	@Override
	public Page<User> findByNameAndBirthYearAndSexAndRegionId(String name, Integer birthYear, Type sex, Long regionId,
			int page, int limit) throws ValidationException {
		LOG.debug("Start fetching users by name '{}', birth year '{}', sex '{}' and region id '{}'", name, birthYear,
				sex, regionId);

		BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "user");

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (birthYear != null) {
			LOG.debug("Validating birth year: {}", birthYear);
			int age = Year.now().getValue() - birthYear;
			if (age < MIN_AGE) {
				LOG.warn("Birth year '{}' is less than min age '{}'", birthYear, MIN_AGE);
				result.rejectValue("birthYear", "user.find.birth_year.less_min_age",
						"Возраст пользователя меньше " + MIN_AGE);
			}
			if (age > MAX_AGE) {
				LOG.warn("Birth year '{}' is more than max age '{}'", birthYear, MAX_AGE);
				result.rejectValue("birthYear", "user.find.birth_year.more_max_age",
						"Возраст пользователя больше " + MAX_AGE);
			}
		}

		if (result.hasErrors()) {
			LOG.warn("The passed data have validation errors: {}", result);
			throw new ValidationException("The passed data are invalid: %s", result, result);
		}

		LOG.debug("The passed data are OK: {}", result);
		Page<User> users = userRepository.findByNameAndBirthYearAndSexAndRegionId(name, birthYear, sex, regionId,
				PageRequest.of(page, limit));

		return users;
	}

	@Override
	public Page<User> findByNameAndRole(String name, long roleId, int page, int limit) throws ValidationException {
		String[] paramNames = new String[] { "name", "roleId", "page", "limit" };

		String logParams = LogTemplate.getParamsTemplate("{}", paramNames);
		LOG.debug("Getting users by " + logParams, name, roleId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "user");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			LOG.warn("Passed data have validation errors: {}. Params: " + logParams, result, name, roleId, page, limit);
			String exceptionParams = LogTemplate.getParamsTemplate("%s", paramNames);
			throw new ValidationException(
					"Exception occurred while fetching users. Passed data are invalid: %s. Params: " + exceptionParams,
					result, name, roleId, page, limit);
		}

		LOG.debug("Passed data are OK. Params: " + logParams, name, roleId, page, limit);

		Page<User> users = userRepository.findByNameAndRole(name, roleId, PageRequest.of(page, limit));
		LOG.info("Got users successfully by params: " + logParams, name, roleId, page, limit);

		return users;
	}

	@Override
	public boolean hasRole(long userId, long roleId) {
		LOG.debug("Checking if user with role exists by user id '{}' and role id '{}'", userId, roleId);
		boolean hasRole = userRepository.hasRole(userId, roleId);
		LOG.info("Got info about belonging role '{}' to user '{}': {}", roleId, userId, hasRole);
		return hasRole;
	}

	@Override
	public boolean hasRole(long userId, Name roleName) {
		LOG.debug("Checking if user with role exists by user id '{}' and role name '{}'", userId, roleName);
		boolean hasRole = userRepository.hasRole(userId, roleName);
		LOG.info("Got info about belonging role '{}' to user '{}': {}", roleName, userId, hasRole);
		return hasRole;
	}

	@Override
	public User save(UserSaveRequest saveRequest)
			throws ValidationException, UserExistException, UserYoungException, UserOldException {
		LOG.debug("Start saving a user: {}", saveRequest);

		LOG.info("Validating the user: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "user");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			LOG.warn("Passed data are invalid. User: {}. Validation result: ", saveRequest, result);
			throw new ValidationException(
					"Exception occurred while saving the user '%s'. Passed data are invalid: '%s'", result, saveRequest,
					result);
		}

		String username = saveRequest.getUsername();
		LOG.debug("Checking username '{}' for existence", username);
		if (userRepository.existsByUsername(username)) {
			LOG.warn("There is a user with username '{}'", username);
			result.rejectValue("username", "user.save.username.exists", "Пользователь с таким именем уже существует");
			throw new UserExistException(new UsernameField(username), result);
		}

		String email = saveRequest.getEmail();
		LOG.debug("Checking email '{}' for existence", email);
		if (userRepository.existsByEmail(email)) {
			LOG.warn("There is a user with email '{}'", email);
			result.rejectValue("email", "user.save.email.exists",
					"Пользователь с такой электронной почтой уже существует");
			throw new UserExistException(new EmailField(email), result);
		}

		LocalDate birthDate = saveRequest.getBirthDate();
		long age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
		LOG.debug("Checking for user is older than {}: {}", MIN_AGE, birthDate);
		if (age < MIN_AGE) {
			LOG.warn("The user is younger than {}. User: {}", MIN_AGE, saveRequest);
			result.rejectValue("birthDate", "user.save.birthdate.young",
					"Пользователи младше " + MIN_AGE + " не допускаются к регистрации на данном ресурсе");
			throw new UserYoungException((int) age, MIN_AGE, result);
		}

		LOG.debug("Checking for user is younger than {}: {}", MAX_AGE, birthDate);
		if (age > MAX_AGE) {
			LOG.warn("The user is older than {}. User: {}", MAX_AGE, saveRequest);
			result.rejectValue("birthDate", "user.save.birthdate.old", "Возраст слишком большой");
			throw new UserOldException((int) age, MAX_AGE, result);
		}

		String password = saveRequest.getPassword();
		LOG.debug("Encrypting the password");
		password = passwordEncoder.encode(password);

		LOG.debug("Creating the user: {}", saveRequest);
		Role role = roleService.findByName(Name.USER);
		User user = new User(username, email, password, role);

		ConfirmCode code = codeService.save(user);
		user.setConfirmCode(code);

		Confidentiality confidentiality = confidentialityService.save(user);
		user.setConfidentiality(confidentiality);

		userRepository.save(user);

		return user;
	}

	public static void main(String[] args) {
//		BindingResult result = new MapBindingResult(new LinkedHashMap<String, String>(), "user");
//		result.reject("a.w.d.a.", "Test message 1");
//		result.reject("a.w.dwwww.", "Test message 2");
//		result.rejectValue("username", "a.w.d.a.", "Test message 3");
//		System.out.println(result);
		String template = "name '%s', role '%s', page '%s' and limit '%s'";
		System.out.println(String.format(String.format(template, "%s", "%s", "%s", "%s"), "a", "v", "c", "d"));
	}

}
