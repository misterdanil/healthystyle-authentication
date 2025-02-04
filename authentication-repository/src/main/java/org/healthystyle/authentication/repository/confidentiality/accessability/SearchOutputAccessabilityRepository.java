package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.SearchOutputAccessability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchOutputAccessabilityRepository extends JpaRepository<SearchOutputAccessability, Long> {
	@Query("SELECT soa FROM SearchOutputAccessability soa WHERE soa.type = :type")
	SearchOutputAccessability findByType(Boolean type);
}
