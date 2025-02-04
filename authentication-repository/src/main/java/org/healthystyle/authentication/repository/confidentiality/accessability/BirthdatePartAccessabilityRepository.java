package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.BirthdatePartAccessability;
import org.healthystyle.model.confidentiality.accessability.type.BirthdatePartAccessabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdatePartAccessabilityRepository extends JpaRepository<BirthdatePartAccessability, Long> {
	@Query("SELECT bpa FROM BirthdatePartAccessability bpa WHERE bpa.type = :type")
	BirthdatePartAccessability findByType(BirthdatePartAccessabilityType type);
}
