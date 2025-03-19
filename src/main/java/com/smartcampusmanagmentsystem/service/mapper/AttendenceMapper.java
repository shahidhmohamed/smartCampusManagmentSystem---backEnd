package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.Attendence;
import com.smartcampusmanagmentsystem.service.dto.AttendenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attendence} and its DTO {@link AttendenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttendenceMapper extends EntityMapper<AttendenceDTO, Attendence> {}
