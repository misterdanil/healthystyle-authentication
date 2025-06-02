package org.healthystyle.authentication.service.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {
//	@Autowired
//	private UserDetailsService userDetailsService;

	@Bean
	@Order(value = Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerFilterChain(HttpSecurity httpSecurity) throws Exception {
//		OAuth2AuthorizationServerConfigurer oauth2AuthorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
//				.authorizationServer();
//		httpSecurity.securityMatcher(oauth2AuthorizationServerConfigurer.getEndpointsMatcher());
////				.with(oauth2AuthorizationServerConfigurer, (authorizationServer) -> {
////					authorizationServer.oidc(Customizer.withDefaults())
////				});
////		OAuth2Authorization.from(null).invalidate(null).build().getRefreshToken().is
//		return httpSecurity.build();
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
		httpSecurity.csrf(csrf -> csrf.disable())/* .userDetailsService(userDetailsService) */;
		DefaultSecurityFilterChain ch = httpSecurity.cors(c -> c.configurationSource(corsConfigurationSource2()))
				.formLogin(Customizer.withDefaults())
				.exceptionHandling(eh -> eh.defaultAuthenticationEntryPointFor(
						new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/**")))

				.build();
		return ch;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource2() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3002", "http://health-service:3001"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
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
