package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.ContactPartAccessability;
import org.healthystyle.model.confidentiality.accessability.type.ContactPartAccessabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPartAccessabilityRepository extends JpaRepository<ContactPartAccessability, Long> {
	@Query("SELECT cpa FROM ContactPartAccessability cpa WHERE cpa.type = :type")
	ContactPartAccessability findByType(ContactPartAccessabilityType type);
}
