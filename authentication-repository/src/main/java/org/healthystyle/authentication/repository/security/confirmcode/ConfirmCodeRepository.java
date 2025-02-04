package org.healthystyle.authentication.repository.security.confirmcode;

import java.time.Instant;

import org.healthystyle.model.security.code.Algorithm;
import org.healthystyle.model.security.code.ConfirmCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Long> {
	@Query("SELECT cc FROM ConfirmCode cc INNER JOIN cc.token t WHERE t.token = :token")
	ConfirmCode findByToken(String token);

	@Query("SELECT cc FROM ConfirmCode cc INNER JOIN cc.user u WHERE u.id = :id")
	ConfirmCode findByUser(Long id);

	@Query("SELECT cc FROM ConfirmCode cc INNER JOIN cc.algorithm a WHERE a = :algorithm")
	Page<ConfirmCode> findByAlgorithm(Algorithm algorithm, Pageable pageable);

	@Query("SELECT cc FROM ConfirmCode cc WHERE cc.issuedAt >= :issuedAt AND cc.expiredAt <= :expiredAt")
	Page<ConfirmCode> findByTime(Instant issuedAt, Instant expiredAt);

	@Modifying
	@Query("DELETE FROM ConfirmCode cc INNER JOIN User u ON u.id = cc.user.id WHERE u.id = :id")
	void deleteByUser(Long id);
}
