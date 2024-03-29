package com.al.qdt.rps.cmd.base;

import com.al.qdt.common.infrastructure.events.rps.GamePlayedEvent;
import com.al.qdt.cqrs.events.BaseEvent;
import com.al.qdt.cqrs.infrastructure.EventStore;
import com.al.qdt.rps.cmd.RpsCmdServiceApp;
import com.al.qdt.rps.cmd.api.commands.PlayGameCommand;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import com.al.qdt.rps.cmd.infrastructure.config.GrpcInProcessConfig;
import com.al.qdt.rps.cmd.infrastructure.config.TestConfig;
import com.al.qdt.rps.grpc.v1.services.GameRequest;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Abstract class for integration tests, allows to reduce the amount of test context restarts.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RpsCmdServiceApp.class,
        properties = {
                "server.servlet.context-path=/rps-cmd-api",
                "api.version-one=/v1",
                "api.endpoint-admin=admin",
                "api.version-one-async=/v1.1",
                "grpc.server.inProcessName=test", // Enable inProcess server
                "grpc.server.port=-1", // Disable external server
                "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
        }
)
// Ensures that the grpc-server is properly shutdown after each test, avoids "port already in use" during tests
@DirtiesContext
@Import({TestConfig.class, GrpcInProcessConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("it")
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTests {

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    AuthenticationService authenticationService;

    // a gRPC channel that provides a connection to a gRPC server on a specified host and port
    @GrpcClient("${grpc.server.inProcessName}")
    protected Channel channel;

    @PostConstruct
    protected void init() {
        // Test injection
        assertNotNull(this.channel);
    }

    @BeforeEach
    void setUp() {
        when(this.authenticationService.getUserId()).thenReturn(UUID.randomUUID());
    }

    protected void setupEnvironment(GameRequest gameRequest) {
        final var playGameCommand = PlayGameCommand.builder()
                .id(TEST_UUID)
                .userId(USER_ONE_ID)
                .hand(com.al.qdt.common.domain.enums.Hand.valueOf(gameRequest.getHand().name()))
                .build();
        final var gamePlayedEvent = GamePlayedEvent.builder()
                .id(playGameCommand.getId())
                .userId(playGameCommand.getUserId())
                .hand(playGameCommand.getHand())
                .build();

        setupEventStore(List.of(gamePlayedEvent));
    }

    protected void setupEventStore(List<BaseEvent> events) {
        final var eventStore = this.applicationContext.getBean(EventStore.class);

        assertNotNull(eventStore);
        when(eventStore.findByAggregateId(any(UUID.class))).thenReturn(events);
    }
}
