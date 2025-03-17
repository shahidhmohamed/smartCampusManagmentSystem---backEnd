package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.GroupChat;
import com.smartcampusmanagmentsystem.service.dto.GroupChatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GroupChat} and its DTO {@link GroupChatDTO}.
 */
@Mapper(componentModel = "spring")
public interface GroupChatMapper extends EntityMapper<GroupChatDTO, GroupChat> {}
