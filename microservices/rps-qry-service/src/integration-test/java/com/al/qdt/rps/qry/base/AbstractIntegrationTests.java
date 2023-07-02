package com.al.qdt.rps.qry.base;

import com.al.qdt.rps.qry.RpsQryServiceApp;
import com.al.qdt.rps.qry.domain.services.security.AuthenticationService;
import com.al.qdt.rps.qry.infrastructure.config.GrpcInProcessConfig;
import com.al.qdt.rps.qry.infrastructure.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.Mockito.when;

/**
 * Abstract class for integration tests, allows to reduce the amount of test context restarts.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RpsQryServiceApp.class,
        properties = {
                "grpc.server.inProcessName=test", // Enable inProcess server
                "grpc.server.port=-1", // Disable external server
                "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
        }
)
// Ensures that the grpc-server is properly shutdown after each test, avoids "port already in use" during tests
@DirtiesContext
@AutoConfigureMockMvc
@Import({TestConfig.class, GrpcInProcessConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("it")
public abstract class AbstractIntegrationTests {

    @Autowired
    AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        when(this.authenticationService.getUserId()).thenReturn(UUID.randomUUID());
    }
}
