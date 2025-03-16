package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.ClassSchedule;
import com.smartcampusmanagmentsystem.service.dto.ClassScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassSchedule} and its DTO {@link ClassScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassScheduleMapper extends EntityMapper<ClassScheduleDTO, ClassSchedule> {}
