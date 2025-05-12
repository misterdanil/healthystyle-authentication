package org.healthystyle.authentication.repository.security.confirmcode;

import java.time.Instant;

import org.healthystyle.model.security.code.ConfirmCode;
import org.healthystyle.model.security.code.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Long> {
	@Query("SELECT cc FROM ConfirmCode cc WHERE cc.token = :token")
	ConfirmCode findByToken(String token);

	@Query("SELECT cc FROM ConfirmCode cc INNER JOIN cc.user u WHERE u.id = :id")
	ConfirmCode findByUser(Long id);

	@Query("SELECT cc FROM ConfirmCode cc INNER JOIN cc.algorithm a WHERE a.type = :type")
	Page<ConfirmCode> findByAlgorithm(Type algorithm, Pageable pageable);

	@Query("SELECT cc FROM ConfirmCode cc WHERE cc.issuedAt >= :issuedAt AND cc.expiredAt <= :expiredAt")
	Page<ConfirmCode> findByTime(Instant issuedAt, Instant expiredAt, Pageable pageable);

	@Modifying
	@Query("DELETE FROM ConfirmCode cc WHERE cc.user.id = :userId")
	void deleteByUser(Long userId);

	@Modifying
	@Query("DELETE FROM ConfirmCode cc WHERE cc.isConfirmed = FALSE AND cc.expiredAt < CURRENT_TIMESTAMP")
	void deleteExpiredAndNotConfirmed();

	@Query("SELECT EXISTS (SELECT cc FROM ConfirmCode cc WHERE cc.token = :token)")
	boolean existsByToken(String token);
}
