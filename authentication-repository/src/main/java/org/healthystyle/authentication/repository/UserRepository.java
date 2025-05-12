package org.healthystyle.authentication.repository;

import java.util.List;

import org.healthystyle.model.User;
import org.healthystyle.model.role.Name;
import org.healthystyle.model.sex.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.id IN :ids")
	Page<User> findByIds(List<Long> ids, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(String username);

	@Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE CONCAT('%', LOWER(:name), '%') OR LOWER(u.username) LIKE CONCAT('%', LOWER(:name), '%')")
	Page<User> findByUsernameAndName(String name, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.telephoneNumber LIKE '%:telephoneNumber%'")
	User findByTelephoneNumber(String telephoneNumber);

	@Query("SELECT u FROM User u WHERE u.email LIKE '%:email%'")
	User findByEmail(String email);

//	@Query("SELECT u FROM User u INNER JOIN Sex s ON u.sex = s WHERE "
//			+ "(:name IS NULL OR (LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(%:name%))) "
//			+ "AND (:birthYear IS NULL OR :birthYear = date_part('year', u.birthDate) AND (:sex IS NULL OR s.type = :type) AND (:regionId IS NULL OR u.regionId = :regionId)")
//	Page<User> findByNameAndBirthYearAndSexAndRegionId(String name, Integer birthYear, Type sex, Long regionId,
//			Pageable pageable);

//	@Query("SELECT u FROM User u INNER JOIN u.roles r ON WHERE (:name IS NULL OR (LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(%:name%))) AND (:roleId IS NULL OR r.id = :roleId)")
//	Page<User> findByNameAndRole(String name, Long roleId, Pageable pageable);

//	@Query("SELECT u FROM User u INNER JOIN RefreshToken rt ON rt.user = u WHERE rt.id = :refreshTokenId")
//	User findByRefreshToken(Long refreshTokenId);

//	@Query("SELECT u FROM User u INNER JOIN RefreshToken rt ON rt.user = u WHERE rt.token = :token")
//	User findByRefreshToken(String token);

	// service findBYUsernameAndName with sort
//	Page<User> findLastCreated(String name, Pageable pageable);

	// service findBYUsernameAndName with sort
	// @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ',
	// u.lastName)) LIKE LOWER(%:name%) OR LOWER(u.username) LIKE LOWER(%:name%)
	// ORDER BY u.removedOn DESC")
//	Page<User> findRemoved(String name, Pageable pageable);

	@Query("SELECT EXISTS (SELECT ur FROM User u INNER JOIN u.roles ur WHERE u.id = :userId AND ur.id = :roleId)")
	boolean hasRole(Long userId, Long roleId);

	@Query("SELECT EXISTS (SELECT ur FROM User u INNER JOIN u.roles ur WHERE u.id = :userId AND ur.name = :roleName)")
	boolean hasRole(Long userId, Name roleName);

	@Query("SELECT EXISTS (SELECT u FROM User u WHERE u.username = :username)")
	boolean existsByUsername(String username);

	@Query("SELECT EXISTS (SELECT u FROM User u WHERE u.email = :email)")
	boolean existsByEmail(String email);
}
