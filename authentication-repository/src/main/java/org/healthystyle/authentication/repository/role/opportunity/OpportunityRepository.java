package org.healthystyle.authentication.repository.role.opportunity;

import org.healthystyle.model.role.opportunity.Name;
import org.healthystyle.model.role.opportunity.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
	@Query("SELECT o FROM Opportunity o WHERE o.name = ':name%'")
	Opportunity findByName(Name name);

//	@Query("WITH RECURSIVE AllOpportunities AS ("
//			+ "SELECT r.*, o,* FROM role r INNER JOIN role_opportunity ro ON ro.role_id = r.id INNER JOIN opportunity o ON ro.opportunity_id = o.id WHERE r.id = :id"
//			+ "UNION ALL"
//			+ "SELECT r.*, o.* FROM AllOpportunities ao INNER JOIN role r ON ao.parent_role_id = r.id INNER JOIN role_opportunity ro ON ro.role_id = r.id INNER JOIN opportunity o ON ro.opportunity_id = o.id"
//			+ ")"
//			+ "SELECT * FROM AllOpportunities")
//	Page<Opportunity> findByRole(Long id);
}
