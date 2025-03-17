package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.Chat;
import com.smartcampusmanagmentsystem.service.dto.ChatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chat} and its DTO {@link ChatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatMapper extends EntityMapper<ChatDTO, Chat> {}
