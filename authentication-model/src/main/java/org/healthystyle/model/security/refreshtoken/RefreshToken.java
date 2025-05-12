package org.healthystyle.model.security.refreshtoken;

import java.time.Instant;
import java.util.Objects;

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

//@Entity
//@Table(name = "refresh_token")
public class RefreshToken {
	@Id
	@SequenceGenerator(name = "refresh_token_id_generator", sequenceName = "refresh_token_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "refresh_token_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 256)
	private String token;
	@ManyToOne
	@JoinColumn(name = "algorithm_id")
	private Algorithm algorithm;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "issued_at", nullable = false)
	private Instant issuedAt;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expired_at", nullable = false)
	private Instant expiredAt;
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public RefreshToken(String token, Instant issuedAt, Instant expiredAt, User user) {
		super();

		Objects.requireNonNull(token, "Token must be not null");
		Objects.requireNonNull(issuedAt, "Issued at must be not null");
		Objects.requireNonNull(expiredAt, "Expired at must be not null");
		Objects.requireNonNull(user, "User must be not null");

		this.token = token;
		this.issuedAt = issuedAt;
		this.expiredAt = expiredAt;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Instant getIssuedAt() {
		return issuedAt;
	}

	public Instant getExpiredAt() {
		return expiredAt;
	}

	public User getUser() {
		return user;
	}
}
