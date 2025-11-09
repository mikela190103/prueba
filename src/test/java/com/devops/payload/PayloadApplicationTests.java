package com.devops.payload;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PayloadApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void applicationContextIsNotEmpty() {
        assertTrue(applicationContext.getBeanDefinitionCount() > 0);
    }

    @Test
    void requiredBeansArePresent() {
        assertTrue(applicationContext.containsBean("securityService"));
        assertTrue(applicationContext.containsBean("jwtService"));
        assertTrue(applicationContext.containsBean("devOpsService"));
        assertTrue(applicationContext.containsBean("devOpsController"));
    }
}
