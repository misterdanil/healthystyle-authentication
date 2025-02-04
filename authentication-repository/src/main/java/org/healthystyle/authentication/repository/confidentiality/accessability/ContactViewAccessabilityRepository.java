package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.ContactViewAccessability;
import org.healthystyle.model.confidentiality.accessability.type.ViewAccessabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactViewAccessabilityRepository extends JpaRepository<ContactViewAccessability, Long> {
	@Query("SELECT cva FROM ContactViewAccessability cva WHERE cva.type = :type")
	ContactViewAccessability findByType(ViewAccessabilityType type);
}
