package com.devops.payload.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Test
    void testSecurityConfigurationBeanExists() {
        assertTrue(applicationContext.containsBean("securityConfiguration"));
    }

    @Test
    void testCorsConfigurationSourceBeanExists() {
        CorsConfigurationSource corsSource = securityConfiguration.corsConfigurationSource();
        assertNotNull(corsSource);
    }

    @Test
    void testCorsConfigurationSourceNotNull() {
        CorsConfigurationSource corsSource = securityConfiguration.corsConfigurationSource();
        assertNotNull(corsSource);
    }
}

