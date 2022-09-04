package com.al.qdt.rps.cmd.base;

import com.al.qdt.rps.cmd.infrastructure.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Abstract class for integration tests of web layer, allows to reduce the amount of test context restarts.
 */
@DirtiesContext
@Import(TestConfig.class)
@ActiveProfiles("it")
public abstract class AbstractWebIntegrationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DisplayName("Testing injections")
    void injectionTest() {
        assertNotNull(this.mockMvc);
        assertNotNull(this.objectMapper);
    }
}
