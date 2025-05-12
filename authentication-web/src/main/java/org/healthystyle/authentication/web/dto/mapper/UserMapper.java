package org.healthystyle.authentication.web.dto.mapper;

import org.healthystyle.authentication.web.dto.UserDto;
import org.healthystyle.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
	UserDto toDto(User user);
}
