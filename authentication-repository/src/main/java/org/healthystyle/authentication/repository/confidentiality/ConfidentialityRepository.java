package org.healthystyle.authentication.repository.confidentiality;

import org.healthystyle.model.confidentiality.Confidentiality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfidentialityRepository extends JpaRepository<Confidentiality, Long> {
	@Query("SELECT c FROM Confidentiality c INNER JOIN c.user u WHERE u.id = :id")
	Confidentiality findByUser(Long id);
}
