package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.Message;
import com.smartcampusmanagmentsystem.service.dto.MessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {}
