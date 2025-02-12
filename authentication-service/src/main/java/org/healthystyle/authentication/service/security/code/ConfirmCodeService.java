package org.healthystyle.authentication.service.security.code;

import java.time.Instant;

import org.healthystyle.authentication.service.error.ValidationException;
import org.healthystyle.authentication.service.error.code.ConfirmCodeNotFoundException;
import org.healthystyle.model.User;
import org.healthystyle.model.security.code.ConfirmCode;
import org.healthystyle.model.security.code.Type;
import org.springframework.data.domain.Page;

public interface ConfirmCodeService {
	ConfirmCode findByToken(String token) throws ConfirmCodeNotFoundException;

	ConfirmCode findByUser(long userId) throws ConfirmCodeNotFoundException;

	Page<ConfirmCode> findByAlgorithm(Type type, int page, int limit) throws ValidationException;

	Page<ConfirmCode> findByTime(Instant issuedAt, Instant expiredAt, int page, int limit) throws ValidationException;

	ConfirmCode save(User user);
	
	ConfirmCode save();

	void confirm(String token) throws ConfirmCodeNotFoundException;

	void deleteByUser(long userId);

	boolean existsByToken(String token);
}
