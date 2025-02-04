package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.BirthdateViewAccessability;
import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BirthdateViewAccessabilityRepository extends JpaRepository<BirthdateViewAccessability, Long> {
	@Query("SELECT bva FROM BirthdateViewAccessability bva WHERE bva.type = :type")
	BirthdateViewAccessability findByType(ViewAccessabilityType type);
}
