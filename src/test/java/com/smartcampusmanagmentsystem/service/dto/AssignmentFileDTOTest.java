package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssignmentFileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentFileDTO.class);
        AssignmentFileDTO assignmentFileDTO1 = new AssignmentFileDTO();
        assignmentFileDTO1.setId("id1");
        AssignmentFileDTO assignmentFileDTO2 = new AssignmentFileDTO();
        assertThat(assignmentFileDTO1).isNotEqualTo(assignmentFileDTO2);
        assignmentFileDTO2.setId(assignmentFileDTO1.getId());
        assertThat(assignmentFileDTO1).isEqualTo(assignmentFileDTO2);
        assignmentFileDTO2.setId("id2");
        assertThat(assignmentFileDTO1).isNotEqualTo(assignmentFileDTO2);
        assignmentFileDTO1.setId(null);
        assertThat(assignmentFileDTO1).isNotEqualTo(assignmentFileDTO2);
    }
}
