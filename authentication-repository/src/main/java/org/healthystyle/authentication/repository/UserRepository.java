package org.healthystyle.authentication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Page<User> findByIds(List<Long> ids);

	User findByUsername(String username);

	User findByName(String name);
	
	Page<User> findByUsernameAndName(String name, Pageable pageable, Sort sort);
 
	User findByTelephoneNumber(String telephoneNumber);

	User findByEmail(String email);

	Page<User> findByNameAndBirthdate(String name, LocalDate birthDate, Pageable pageable);

	Page<User> findByRole(Long roleId, Pageable pageable);

	Page<User> findByNameAndRole(String name, Long roleId, Pageable pageable);

	User findByRefreshToken(String token);

	Page<User> findByNameAndRegionId(String name, Long regionId, Pageable pageable);

	Page<User> findLastCreated(Pageable pageable);

	Page<User> findLastCreated(String name, Pageable pageable);

	Page<User> findByRegionId(Long regionId, Pageable pageable);

	Page<User> findRemoved(String name, Pageable pageable);

}
