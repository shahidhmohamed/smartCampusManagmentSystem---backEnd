package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModuleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModuleDTO.class);
        ModuleDTO moduleDTO1 = new ModuleDTO();
        moduleDTO1.setId("id1");
        ModuleDTO moduleDTO2 = new ModuleDTO();
        assertThat(moduleDTO1).isNotEqualTo(moduleDTO2);
        moduleDTO2.setId(moduleDTO1.getId());
        assertThat(moduleDTO1).isEqualTo(moduleDTO2);
        moduleDTO2.setId("id2");
        assertThat(moduleDTO1).isNotEqualTo(moduleDTO2);
        moduleDTO1.setId(null);
        assertThat(moduleDTO1).isNotEqualTo(moduleDTO2);
    }
}
