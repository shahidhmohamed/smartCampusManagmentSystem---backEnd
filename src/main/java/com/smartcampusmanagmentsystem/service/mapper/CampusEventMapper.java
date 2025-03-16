package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.CampusEvent;
import com.smartcampusmanagmentsystem.service.dto.CampusEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CampusEvent} and its DTO {@link CampusEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface CampusEventMapper extends EntityMapper<CampusEventDTO, CampusEvent> {}
