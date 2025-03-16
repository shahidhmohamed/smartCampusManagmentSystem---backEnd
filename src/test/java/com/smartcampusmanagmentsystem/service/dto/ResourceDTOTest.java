package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceDTO.class);
        ResourceDTO resourceDTO1 = new ResourceDTO();
        resourceDTO1.setId("id1");
        ResourceDTO resourceDTO2 = new ResourceDTO();
        assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
        resourceDTO2.setId(resourceDTO1.getId());
        assertThat(resourceDTO1).isEqualTo(resourceDTO2);
        resourceDTO2.setId("id2");
        assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
        resourceDTO1.setId(null);
        assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
    }
}
