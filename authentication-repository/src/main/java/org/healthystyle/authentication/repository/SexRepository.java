package org.healthystyle.authentication.repository;

import org.healthystyle.model.sex.Sex;
import org.healthystyle.model.sex.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SexRepository extends JpaRepository<Sex, Long> {
	@Query("SELECT s FROM Sex s WHERE s.type = :type")
	Sex findByType(Type type);
}
