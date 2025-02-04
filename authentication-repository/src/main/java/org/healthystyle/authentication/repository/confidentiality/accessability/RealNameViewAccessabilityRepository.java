package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.RealNameViewAccessability;
import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RealNameViewAccessabilityRepository extends JpaRepository<RealNameViewAccessability, Long> {
	@Query("SELECT rnva FROM RealNameViewAccessability rnva WHERE rnva.type = :type")
	RealNameViewAccessability findByType(ViewAccessabilityType type);
}
