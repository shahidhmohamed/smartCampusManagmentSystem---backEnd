package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.ClassScheduleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassSchedule.class);
        ClassSchedule classSchedule1 = getClassScheduleSample1();
        ClassSchedule classSchedule2 = new ClassSchedule();
        assertThat(classSchedule1).isNotEqualTo(classSchedule2);

        classSchedule2.setId(classSchedule1.getId());
        assertThat(classSchedule1).isEqualTo(classSchedule2);

        classSchedule2 = getClassScheduleSample2();
        assertThat(classSchedule1).isNotEqualTo(classSchedule2);
    }
}
