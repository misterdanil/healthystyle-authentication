package org.healthystyle.authentication.service.security.code;

import java.time.Instant;

import org.healthystyle.authentication.service.error.ValidationException;
import org.healthystyle.model.security.code.ConfirmCode;
import org.healthystyle.model.security.code.Type;
import org.springframework.data.domain.Page;

public interface ConfirmCodeService {
	ConfirmCode findByToken(String token);

	ConfirmCode findByUser(long userId);

	Page<ConfirmCode> findByAlgorithm(Type type, int page, int limit);

	Page<ConfirmCode> findByTime(Instant issuedAt, Instant expiredAt, int page, int limit) throws ValidationException;

	void confirm(String token);

	void deleteByUser(long userId);

	boolean existsByToken(String token);
}
