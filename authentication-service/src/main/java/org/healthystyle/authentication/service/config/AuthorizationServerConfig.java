package org.healthystyle.authentication.service.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {
	@Bean
	@Order(value = Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerFilterChain(HttpSecurity httpSecurity) throws Exception {
		OAuth2AuthorizationServerConfigurer oauth2AuthorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
				.authorizationServer();
		httpSecurity.securityMatcher(oauth2AuthorizationServerConfigurer.getEndpointsMatcher())
				.with(oauth2AuthorizationServerConfigurer, (authorizationServer) -> {
					authorizationServer.oidc(Customizer.withDefaults()).
				});
		OAuth2Authorization.from(null).invalidate(null).build().getRefreshToken().is
		return httpSecurity.build();
	}

	@Bean
	public InMemoryRegisteredClientRepository registeredClientRepository() {
		RegisteredClient healthClient = RegisteredClient.withId(UUID.randomUUID().toString()).clientId("health")
				.clientSecret("{noop}123456789")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN).redirectUri("http://localhost:8081/code")
				.tokenSettings(TokenSettings.builder().authorizationCodeTimeToLive(Duration.ofMinutes(5))
						.accessTokenTimeToLive(Duration.ofMinutes(30)).refreshTokenTimeToLive(Duration.ofDays(30))
						.reuseRefreshTokens(false).build())
				.build();

		return new InMemoryRegisteredClientRepository(healthClient);
	}

	@Bean
	public JdbcRegisteredClientRepository jdbcRegisteredClientRepository(JdbcOperations operations) {
		RegisteredClient healthClient = RegisteredClient.withId(UUID.randomUUID().toString()).clientId("health")
				.clientSecret("{noop}123456789")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN).redirectUri("http://localhost:8081/code")
				.tokenSettings(TokenSettings.builder().authorizationCodeTimeToLive(Duration.ofMinutes(5))
						.accessTokenTimeToLive(Duration.ofMinutes(30)).refreshTokenTimeToLive(Duration.ofDays(30))
						.reuseRefreshTokens(false).build())
				.build();

		return new JdbcRegisteredClientRepository(operations);
	}

	@Bean
	public JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService(JdbcOperations operations,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationService(operations, registeredClientRepository);
	}

	@Bean
	public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource());
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
		RSAKey rsaKey = generateRSA();
		JWKSet jwkSet = new JWKSet(rsaKey);

		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	public RSAKey generateRSA() throws NoSuchAlgorithmException {
		KeyPair keyPair = generateRSAKey();

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString())
				.algorithm(JWSAlgorithm.RS256).build();
	}

	public KeyPair generateRSAKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);

		return keyPairGenerator.generateKeyPair();
	}
}
