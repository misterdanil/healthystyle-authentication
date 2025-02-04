package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.ModeratorWarnedAccessability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorWarnedAccessabilityRepository extends JpaRepository<ModeratorWarnedAccessability, Long> {
	@Query("SELECT mwa FROM ModeratorWarnedAccessability mwa WHERE mwa.type = :type")
	ModeratorWarnedAccessability findByType(Boolean type);
}
