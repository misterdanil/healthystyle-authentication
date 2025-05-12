package org.healthystyle.authentication.repository.role;

import org.healthystyle.model.role.Name;
import org.healthystyle.model.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query("SELECT r FROM Role r WHERE r.name = :name")
	Role findByName(Name name);

//	@Query("SELECT r FROM Role r WHERE r.parent.id = :parentRoleId")
//	Page<Role> findByParentRole(Long parentRoleId, Pageable pageable, Sort sort);
//	
//	@Query("SELECT r.parent FROM Role r WHERE r.id = :id")
//	Role findParentRole(Long id);
//	
//	@Query(value = "WITH RECURSIVE Parents AS ("
//			+ "SELECT p FROM Role AS r INNER JOIN Role AS p ON r.parent_role_id = p.id WHERE r.id = :id"
//			+ "UNION ALL"
//			+ "SELECT r FROM Role AS r INNER JOIN Parents AS p ON p.parent_role_id = r.id"
//			+ ")"
//			+ "SELECT * FROM Parents", nativeQuery = true)
//	Page<Role> findAllParents(Long id);
//	
//	@Query(value = "WITH RECURSIVE Children AS ("
//			+ "SELECT r FROM Role AS r INNER JOIN Role AS c ON c.parent_role_id = r.id WHERE r.id = :id"
//			+ "UNION ALL"
//			+ "SELECT r FROM Role AS r INNER JOIN Children AS c ON r.parent_role_id = c.id"
//			+ ")"
//			+ "SELECT * FROM Children", nativeQuery = true)
//	Page<Role> findAllChildren(Long id);
//	
//	@Query(value = "WITH RECURSIVE OpportunityRoles AS ("
//			+ "SELECT r FROM role AS r INNER JOIN role_opportunity AS ro ON ro.role_id = r.id INNER JOIN opportunity AS o ON ro.opportunity_id = o.id WHERE o.name = :name"
//			+ "UNION ALL"
//			+ "SELECT r FROM role AS r INNER JOIN OpportunityRoles AS or ON or.id = r.parent_role_id"
//			+ ")"
//			+ "SELECT * FROM OpportunityRoles", nativeQuery = true)
//	Page<Role> findByIncludingOpportunity(Pageable pageable, org.healthystyle.model.role.opportunity.Name name);

}
