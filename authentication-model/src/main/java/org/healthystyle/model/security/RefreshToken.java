package org.healthystyle.model.security;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
	@Id
	@SequenceGenerator(name = "refresh_token_id_generator", sequenceName = "refresh_token_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "refresh_token_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 256)
	private String token;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "issued_at", nullable = false)
	private Instant issuedAt;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expired_at", nullable = false)
	private Instant expiredAt;
	@OneToOne(mappedBy = "refreshToken")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public RefreshToken(String token, Instant issuedAt, Instant expiredAt, User user) {
		super();

		Objects.requireNonNull(token);
		Objects.requireNonNull(issuedAt);
		Objects.requireNonNull(expiredAt);
		Objects.requireNonNull(user);

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
