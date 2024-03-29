package com.al.qdt.score.cmd.base

import com.al.qdt.score.cmd.ScoreCmdServiceApp
import com.al.qdt.score.cmd.domain.services.security.AuthenticationService
import com.al.qdt.score.cmd.infrastructure.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Trait for integration tests, allows to reduce the amount of test context restarts.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [ScoreCmdServiceApp.class]
)
// Ensures that the grpc-server is properly shutdown after each test, avoids "port already in use" during tests
@DirtiesContext
@AutoConfigureMockMvc
@Stepwise
@ActiveProfiles("it")
@Import(TestConfig.class)
class AbstractIntegrationTests extends Specification {

    @Autowired
    AuthenticationService authenticationService

    @Autowired
    MockMvc mockMvc

    void setup() {
        when: 'Calling the auth service'
        authenticationService.getUserId()

        then: 'Return randomly generated user id'
        UUID.randomUUID()
    }

    def 'Testing injections'() {
        expect:
        assert mockMvc
    }
}
