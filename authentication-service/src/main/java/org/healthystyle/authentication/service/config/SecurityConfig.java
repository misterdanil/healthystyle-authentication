package org.healthystyle.authentication.service.config;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.authorizeHttpRequests(http -> http.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/signup", "/signin", "/auth/checking", "/loginPage", "/assets/*").permitAll()
						.anyRequest().authenticated())
				.formLogin(fl -> fl.loginPage("/login").permitAll()).csrf(csrf -> csrf.disable())
				.cors(c -> c.configurationSource(corsConfigurationSource()))

		/*
		 * .exceptionHandling(eh -> eh.defaultAuthenticationEntryPointFor( new
		 * LoginUrlAuthenticationEntryPoint("http://localhost:3000/auth"), new
		 * AntPathRequestMatcher("/**")))
		 */;

		httpSecurity.oauth2ResourceServer(rs -> rs.jwt(Customizer.withDefaults()));
		httpSecurity.userDetailsService(userDetailsService);

		DefaultSecurityFilterChain c = httpSecurity.build();
		return c;
	}

	@Bean
	public CustomSavedRequestAwareAuthenticationSuccessHandler customSavedRequestAwareAuthenticationSuccessHandler() {
		return new CustomSavedRequestAwareAuthenticationSuccessHandler();
	}

	public static class CustomSavedRequestAwareAuthenticationSuccessHandler
			extends SavedRequestAwareAuthenticationSuccessHandler {
		private RequestCache requestCache = new HttpSessionRequestCache();

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws ServletException, IOException {
			SavedRequest savedRequest = this.requestCache.getRequest(request, response);
			if (savedRequest == null) {
				super.onAuthenticationSuccess(request, response, authentication);
				return;
			}
			String targetUrlParameter = getTargetUrlParameter();
			if (isAlwaysUseDefaultTargetUrl()
					|| (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
				this.requestCache.removeRequest(request, response);
				super.onAuthenticationSuccess(request, response, authentication);
				return;
			}
			clearAuthenticationAttributes(request);
			// Use the DefaultSavedRequest URL
			String targetUrl = savedRequest.getRedirectUrl();
			response.getWriter().write(targetUrl);
		}

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3002"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public InMemoryRegisteredClientRepository registeredClientRepository() {
		RegisteredClient healthClient = RegisteredClient.withId("1").clientId("health")
				.clientSecret(passwordEncoder().encode("123456789"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("http://localhost:3001/auth/health").scope(OidcScopes.OPENID)
				.tokenSettings(TokenSettings.builder().authorizationCodeTimeToLive(Duration.ofMinutes(5))
						.accessTokenTimeToLive(Duration.ofMinutes(30)).refreshTokenTimeToLive(Duration.ofDays(30))
						.reuseRefreshTokens(false).build())
				.build();

		RegisteredClient eventClient = RegisteredClient.withId("2").clientId("event")
				.clientSecret(passwordEncoder().encode("123456789"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("http://localhost:3002/auth/event").scope(OidcScopes.OPENID)
				.tokenSettings(TokenSettings.builder().authorizationCodeTimeToLive(Duration.ofMinutes(5))
						.accessTokenTimeToLive(Duration.ofMinutes(30)).refreshTokenTimeToLive(Duration.ofDays(30))
						.reuseRefreshTokens(false).build())
				.build();
		RegisteredClient client = RegisteredClient.withId("3").clientId("johnsteve")
				.clientSecret(passwordEncoder().encode("123456789"))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("https://oidcdebugger.com/debug").scope(OidcScopes.OPENID)
				.tokenSettings(TokenSettings.builder().authorizationCodeTimeToLive(Duration.ofMinutes(30))
						.accessTokenTimeToLive(Duration.ofMinutes(30)).reuseRefreshTokens(false).build())
				.build();

		return new InMemoryRegisteredClientRepository(healthClient, eventClient, client);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withUsername("John").password(passwordEncoder().encode("147852369")).roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user);
	}

	public static void main(String[] args) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String password = "147852369";
		String encoded = encoder.encode(password);
		System.out.println(encoder.matches(password, encoded));
	}
}
