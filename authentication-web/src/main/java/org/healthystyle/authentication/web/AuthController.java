package org.healthystyle.authentication.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.healthystyle.authentication.service.UserService;
import org.healthystyle.authentication.service.dto.UserSaveRequest;
import org.healthystyle.authentication.service.error.ValidationException;
import org.healthystyle.authentication.service.error.user.UserExistException;
import org.healthystyle.authentication.service.error.user.UserOldException;
import org.healthystyle.authentication.service.error.user.UserYoungException;
import org.healthystyle.authentication.web.dto.mapper.UserMapper;
import org.healthystyle.model.User;
import org.healthystyle.util.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
	@Autowired
	private UserService service;

	@Autowired
	private UserMapper mapper;

	private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	@GetMapping("/auth/checking")
	@ResponseBody
	public ResponseEntity<Boolean> checkAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return ResponseEntity
				.ok(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken));
	}

	@PostMapping("/signup")
	@ResponseBody
	public ResponseEntity<?> register(@RequestBody UserSaveRequest saveRequest, HttpServletRequest request,
			HttpServletResponse response) throws URISyntaxException {
		try {
			User user = service.save(saveRequest);

			securityContextRepository.saveContext(
					new SecurityContextImpl(new UsernamePasswordAuthenticationToken(user.getUsername(),
							user.getPassword(), user.getRoles().stream()
									.map(r -> new SimpleGrantedAuthority(r.getName().name())).toList())),
					request, response);
			return ResponseEntity.created(new URI("/users/" + user.getId())).build();

		} catch (UserOldException | ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (UserExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (UserYoungException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		}
	}

	@GetMapping("/loginPage")
	public String get() {
		return "/index.html";
	}

	@GetMapping(value = "/users", params = "username")
	public ResponseEntity<?> find(@RequestParam String username, @RequestParam int page, @RequestParam int limit) {
		Page<User> users;
		try {
			users = service.findByUsernameAndName(username, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(users.map(mapper::toDto));
	}

	@GetMapping("/id")
	@ResponseBody
	public String getId() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@GetMapping("/username")
	@ResponseBody
	public String getUsername() {
		Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
		String username = service.fetchUsernameById(id);

		return username;
	}

	@GetMapping(value = "/users", params = "ids")
	public ResponseEntity<?> getUsersByIds(@RequestParam List<Long> ids, @RequestParam int page,
			@RequestParam int limit) {
		Page<User> users = service.findByIds(ids, page, limit);

		return ResponseEntity.ok(users.map(mapper::toDto));
	}
}
