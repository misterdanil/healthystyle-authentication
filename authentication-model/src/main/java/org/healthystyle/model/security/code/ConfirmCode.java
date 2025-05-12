package org.healthystyle.model.security.code;

import java.time.Instant;

import org.healthystyle.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "confirm_code")
public class ConfirmCode {
	@Id
	@SequenceGenerator(name = "confirm_code_id_generator", sequenceName = "confirm_code_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "confirm_code_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, unique = true/*
											 * , columnDefinition =
											 * "VARCHAR(6) CONSTRAINT CK_valid_confirm_code_token CHECK (token ~ '\\d{6}')"
											 */)
	private String token;
	@Column(name = "is_confirmed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isConfirmed = false;
	@ManyToOne
	@JoinColumn(name = "algorithm_id")
	private Algorithm algorithm;
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "issued_at", nullable = false)
	private Instant issuedAt;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expired_at", nullable = false)
	private Instant expiredAt;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn = Instant.now();

	public ConfirmCode() {
		super();
	}

	public ConfirmCode(String token, Algorithm algorithm, User user, Instant issuedAt, Instant expiredAt) {
		super();

		this.token = token;
		this.algorithm = algorithm;
		this.user = user;
		this.issuedAt = issuedAt;
		this.expiredAt = expiredAt;
	}

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public Boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(Boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public User getUser() {
		return user;
	}

	public Instant getIssuedAt() {
		return issuedAt;
	}

	public Instant getExpiredAt() {
		return expiredAt;
	}
}
