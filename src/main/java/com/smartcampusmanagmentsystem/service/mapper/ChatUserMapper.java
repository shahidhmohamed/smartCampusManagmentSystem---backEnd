package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.ChatUser;
import com.smartcampusmanagmentsystem.service.dto.ChatUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatUser} and its DTO {@link ChatUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatUserMapper extends EntityMapper<ChatUserDTO, ChatUser> {}
