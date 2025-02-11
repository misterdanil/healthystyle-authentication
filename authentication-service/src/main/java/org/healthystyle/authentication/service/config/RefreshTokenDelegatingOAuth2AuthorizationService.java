package org.healthystyle.authentication.service.config;

import org.healthystyle.authentication.service.security.refreshtoken.RefreshTokenService;
import org.healthystyle.model.security.refreshtoken.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

@Service
// OAuth2AuthorizationService must save refresh token to DB but default realization (memory) saves everything in memory
public class RefreshTokenDelegatingOAuth2AuthorizationService implements OAuth2AuthorizationService {
	@Autowired
	private RefreshTokenService refreshTokenService;
	private OAuth2AuthorizationService authorizationService;

	private static final String SAVED_REFRESH_TOKEN = "SAVED_RT";

	@Override
	public void save(OAuth2Authorization authorization) {
		authorizationService.save(authorization);
		Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		if (refreshToken.isActive() && authorization.getAttribute(SAVED_REFRESH_TOKEN) == null) {
			RefreshToken savedRefreshToken = refreshTokenService.save(refreshToken.getToken());
			authorization.getAttributes().put(SAVED_REFRESH_TOKEN, savedRefreshToken.getToken());
		}

	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
		refreshTokenService.delete(refreshToken.getToken().getTokenValue());
		authorization.getAttributes().remove(SAVED_REFRESH_TOKEN);
		authorizationService.remove(authorization);
	}

	@Override
	public OAuth2Authorization findById(String id) {
		return authorizationService.findById(id);
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, tokenType);
		if (authorization == null && tokenType.equals(OAuth2TokenType.REFRESH_TOKEN)) {
			OAuth2Authorization.withRegisteredClient(null).build();
		}
	}

}
