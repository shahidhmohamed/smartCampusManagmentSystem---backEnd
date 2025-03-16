package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.AssignmentFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssignmentFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentFile.class);
        AssignmentFile assignmentFile1 = getAssignmentFileSample1();
        AssignmentFile assignmentFile2 = new AssignmentFile();
        assertThat(assignmentFile1).isNotEqualTo(assignmentFile2);

        assignmentFile2.setId(assignmentFile1.getId());
        assertThat(assignmentFile1).isEqualTo(assignmentFile2);

        assignmentFile2 = getAssignmentFileSample2();
        assertThat(assignmentFile1).isNotEqualTo(assignmentFile2);
    }
}
