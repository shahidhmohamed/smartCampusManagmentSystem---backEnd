package com.smartcampusmanagmentsystem.service.mapper;

import com.smartcampusmanagmentsystem.domain.Resource;
import com.smartcampusmanagmentsystem.domain.ResourceBooking;
import com.smartcampusmanagmentsystem.service.dto.ResourceBookingDTO;
import com.smartcampusmanagmentsystem.service.dto.ResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceBooking} and its DTO {@link ResourceBookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceBookingMapper extends EntityMapper<ResourceBookingDTO, ResourceBooking> {
    @Mapping(target = "resource", source = "resource", qualifiedByName = "resourceId")
    ResourceBookingDTO toDto(ResourceBooking s);

    @Named("resourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResourceDTO toDtoResourceId(Resource resource);
}
