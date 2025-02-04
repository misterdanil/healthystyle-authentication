package org.healthystyle.authentication.repository.security.refreshtoken;

import org.healthystyle.model.security.code.Algorithm;
import org.healthystyle.model.security.refreshtoken.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
	@Query("SELECT a FROM Algorithm a WHERE a.type = :type")
	Algorithm findByType(Type type);
}
