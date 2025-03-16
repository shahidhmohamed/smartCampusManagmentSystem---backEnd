package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.AssignmentFile;
import com.smartcampusmanagmentsystem.service.dto.AssignmentFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssignmentFile} and its DTO {@link AssignmentFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssignmentFileMapper extends EntityMapper<AssignmentFileDTO, AssignmentFile> {}
