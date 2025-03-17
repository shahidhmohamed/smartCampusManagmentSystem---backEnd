package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.GroupChatMembers;
import com.smartcampusmanagmentsystem.service.dto.GroupChatMembersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GroupChatMembers} and its DTO {@link GroupChatMembersDTO}.
 */
@Mapper(componentModel = "spring")
public interface GroupChatMembersMapper extends EntityMapper<GroupChatMembersDTO, GroupChatMembers> {}
