package org.healthystyle.authentication.repository.confidentiality.accessability;

import org.healthystyle.model.confidentiality.accessability.ModeratorBannedAccessability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorBannedAccessabilityRepository {
	@Query("SELECT mba FROM ModeratorBannedAccessability mba WHERE mba.type = :type")
	ModeratorBannedAccessability findByType(Boolean type);
}
