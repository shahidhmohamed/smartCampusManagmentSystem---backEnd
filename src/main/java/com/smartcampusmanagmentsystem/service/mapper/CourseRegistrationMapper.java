package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.CourseRegistration;
import com.smartcampusmanagmentsystem.service.dto.CourseRegistrationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CourseRegistration} and its DTO {@link CourseRegistrationDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseRegistrationMapper extends EntityMapper<CourseRegistrationDTO, CourseRegistration> {}
