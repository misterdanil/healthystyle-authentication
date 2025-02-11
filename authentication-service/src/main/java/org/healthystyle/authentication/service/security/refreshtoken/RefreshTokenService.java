package org.healthystyle.authentication.service.security.refreshtoken;

import org.healthystyle.model.security.refreshtoken.RefreshToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
	RefreshToken findById(Long id);

	RefreshToken findByToken(String token);

	RefreshToken save(OAuth2RefreshToken refreshToken);

	void delete(Long id);

	void delete(String token);

	void deleteByUser(Long userId);

}
