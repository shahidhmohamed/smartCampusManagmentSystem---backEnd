package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FolderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FolderDTO.class);
        FolderDTO folderDTO1 = new FolderDTO();
        folderDTO1.setId("id1");
        FolderDTO folderDTO2 = new FolderDTO();
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
        folderDTO2.setId(folderDTO1.getId());
        assertThat(folderDTO1).isEqualTo(folderDTO2);
        folderDTO2.setId("id2");
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
        folderDTO1.setId(null);
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
    }
}
