package com.zikstock.demo.spring.boot.infrastructure.security;

import com.zikstock.demo.spring.boot.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Security test that verifies endpoints are protected.
 * This test runs with the production security configuration (OAuth2).
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.security.oauth2.client.registration.google.client-id=test-client-id",
        "spring.security.oauth2.client.registration.google.client-secret=test-client-secret"
})
class SecurityConfigTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new org.springframework.web.client.DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(org.springframework.http.client.ClientHttpResponse response) {
                return false; // Don't throw exceptions for any status code
            }
        });
        baseUrl = "http://localhost:" + port + "/api/zikresources";
    }

    @Test
    void should_redirect_to_login_when_accessing_protected_endpoint_without_authentication() {
        // When: accessing a protected endpoint without authentication
        var response = restTemplate.getForEntity(baseUrl, String.class);

        // Then: should get a redirect to the OAuth2 login page
        assertThat(response.getHeaders().getLocation()).isNotNull();
        // The redirect goes to Google's OAuth server
        assertThat(response.getHeaders().getLocation().toString())
                .contains("accounts.google.com/o/oauth2")
                .contains("client_id=test-client-id");
    }
}
