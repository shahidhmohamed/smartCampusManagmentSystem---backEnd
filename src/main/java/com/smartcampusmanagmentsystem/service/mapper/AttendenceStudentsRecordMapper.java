package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecord;
import com.smartcampusmanagmentsystem.service.dto.AttendenceStudentsRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttendenceStudentsRecord} and its DTO {@link AttendenceStudentsRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttendenceStudentsRecordMapper extends EntityMapper<AttendenceStudentsRecordDTO, AttendenceStudentsRecord> {}
