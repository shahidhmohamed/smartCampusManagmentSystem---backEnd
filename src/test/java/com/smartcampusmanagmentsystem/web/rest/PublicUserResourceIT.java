package com.smartcampusmanagmentsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.smartcampusmanagmentsystem.IntegrationTest;
import com.smartcampusmanagmentsystem.domain.User;
import com.smartcampusmanagmentsystem.repository.UserRepository;
import com.smartcampusmanagmentsystem.repository.search.UserSearchRepository;
import com.smartcampusmanagmentsystem.security.AuthoritiesConstants;
import com.smartcampusmanagmentsystem.service.UserService;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PublicUserResource} REST controller.
 */
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_TIMEOUT)
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class PublicUserResourceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * This repository is mocked in the com.smartcampusmanagmentsystem.repository.search test package.
     *
     * @see com.smartcampusmanagmentsystem.repository.search.UserSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserSearchRepository mockUserSearchRepository;

    @Autowired
    private WebTestClient webTestClient;

    private User user;
    private Long numberOfUsers;

    @BeforeEach
    public void countUsers() {
        numberOfUsers = userRepository.count().block();
    }

    @BeforeEach
    public void initTest() {
        user = UserResourceIT.initTestUser();
    }

    @AfterEach
    public void cleanupAndCheck() {
        userService.deleteUser(user.getLogin()).block();
        assertThat(userRepository.count().block()).isEqualTo(numberOfUsers);
        numberOfUsers = null;
    }

    @Test
    void getAllPublicUsers() {
        // Initialize the database
        userRepository.save(user).block();

        // Get all the users
        webTestClient
            .get()
            .uri("/api/users?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[?(@.id == '%s')].login", user.getId())
            .isEqualTo(user.getLogin())
            .jsonPath("$.[?(@.id == '%s')].keys()", user.getId())
            .isEqualTo(Set.of("id", "login"))
            .jsonPath("$.[*].email")
            .doesNotHaveJsonPath()
            .jsonPath("$.[*].imageUrl")
            .doesNotHaveJsonPath()
            .jsonPath("$.[*].langKey")
            .doesNotHaveJsonPath();
    }
}
