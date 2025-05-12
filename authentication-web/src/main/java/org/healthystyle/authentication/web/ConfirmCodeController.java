package org.healthystyle.authentication.web;

import org.healthystyle.authentication.service.error.code.ConfirmCodeNotFoundException;
import org.healthystyle.authentication.service.security.code.ConfirmCodeService;
import org.healthystyle.util.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfirmCodeController {
	@Autowired
	private ConfirmCodeService service;

	@PutMapping("/code/{token}/confirm")
	public ResponseEntity<?> confirm(@PathVariable String token) {
		try {
			service.confirm(token);
			return ResponseEntity.ok().build();
		} catch (ConfirmCodeNotFoundException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("1001", e.getGlobalErrors(), e.getFieldErrors()));
		}
	}
}
