package org.healthystyle.authentication.repository.security.refreshtoken;

import java.time.Instant;

import org.healthystyle.model.security.code.Algorithm;
import org.healthystyle.model.security.code.ConfirmCode;
import org.healthystyle.model.security.refreshtoken.RefreshToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//@Repository
public interface RefreshTokenRepository /* extends JpaRepository<RefreshToken, Long> */ {
	@Query("SELECT t FROM Token t WHERE t.token = :token")
	RefreshToken findByToken(String token);

	@Query("SELECT t FROM Token t INNER JOIN t.user u WHERE t.id = :id")
	RefreshToken findByUser(Long id);

	@Query("SELECT rt FROM RefreshToken rt INNER JOIN rt.algorithm a WHERE a = :algorithm")
	Page<RefreshToken> findByAlgorithm(Algorithm algorithm, Pageable pageable);

	@Query("SELECT rt FROM RefreshToken rt WHERE rt.issuedAt >= :issuedAt AND rt.expiredAt <= :expiredAt")
	Page<RefreshToken> findByTime(Instant issuedAt, Instant expiredAt);

	@Modifying
	@Query("DELETE FROM RefreshToken rt INNER JOIN rt.user u WHERE u.id = :id")
	void deleteByUser(Long id);
}
