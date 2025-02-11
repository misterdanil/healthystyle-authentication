package org.healthystyle.authentication.service;

import java.util.List;

import org.healthystyle.authentication.service.dto.UserSaveRequest;
import org.healthystyle.authentication.service.error.ValidationException;
import org.healthystyle.authentication.service.error.user.UserExistException;
import org.healthystyle.authentication.service.error.user.UserNotFoundException;
import org.healthystyle.authentication.service.error.user.UserOldException;
import org.healthystyle.authentication.service.error.user.UserYoungException;
import org.healthystyle.model.User;
import org.healthystyle.model.sex.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface UserService {
	User findById(long id) throws UserNotFoundException;

	Page<User> findByIds(List<Long> ids);

	User findByUsername(String username) throws UserNotFoundException;

	Page<User> findByUsernameAndName(String name, int page, int limit, Sort sort) throws ValidationException;

	User findByTelephoneNumber(String telephoneNumber) throws UserNotFoundException;

	User findByEmail(String email) throws UserNotFoundException;

	Page<User> findByNameAndBirthYearAndSexAndRegionId(String name, Integer birthYear, Type sex, Long regionId,
			int page, int limit) throws ValidationException;

	Page<User> findByNameAndRole(String name, long roleId, int page, int limit) throws ValidationException;

//	User findByRefreshToken(Long refreshTokenId);

//	User findByRefreshToken(String token);

	// service findBYUsernameAndName with sort
//	Page<User> findLastCreated(String name, Pageable pageable);

	// service findBYUsernameAndName with sort
	// @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ',
	// u.lastName)) LIKE LOWER(%:name%) OR LOWER(u.username) LIKE LOWER(%:name%)
	// ORDER BY u.removedOn DESC")
//	Page<User> findRemoved(String name, Pageable pageable);


	boolean hasRole(long userId, long roleId);

	User save(UserSaveRequest saveRequest) throws ValidationException, UserExistException, UserYoungException, UserOldException;
}
