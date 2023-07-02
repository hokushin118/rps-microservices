package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.api.advices.GlobalRestExceptionHandler;
import com.al.qdt.rps.cmd.api.advices.InvalidUserInputExceptionHandler;
import com.al.qdt.rps.cmd.base.ProtoTests;
import com.al.qdt.rps.cmd.domain.services.RpsServiceV2;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import com.al.qdt.rps.grpc.v1.services.GameRequest;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.rps.grpc.v1.common.Hand.ROCK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing of the RpsControllerV2 controller")
@Tag(value = "controller")
class RpsControllerV2Test implements ProtoTests {

    private static ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter;

    MockMvc mockMvc;

    @Mock
    RpsServiceV2 rpsService;

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    RpsControllerV2 rpsController;

    @Captor
    ArgumentCaptor<GameRequest> gameRequestArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> idArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> userIdArgumentCaptor;

    @BeforeAll
    static void init() {
        final var parser = JsonFormat.parser()
                .ignoringUnknownFields();
        final var printer = JsonFormat.printer()
                .includingDefaultValueFields();
        protobufJsonFormatHttpMessageConverter = new ProtobufJsonFormatHttpMessageConverter(parser, printer);
    }

    @BeforeEach
    void setUp() {
        this.gameRequestArgumentCaptor = ArgumentCaptor.forClass(GameRequest.class);
        this.idArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        lenient().when(this.authenticationService.getUserId()).thenReturn(UUID.randomUUID());

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.rpsController)
                .addPlaceholderValue("api.version-two", "/v2")
                .addPlaceholderValue("api.version-two-async", "/v2.1")
                .addPlaceholderValue("api.endpoint-games", "games")
                .addPlaceholderValue("api.endpoint-admin", "admin")
                .setMessageConverters(protobufJsonFormatHttpMessageConverter)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setControllerAdvice(new InvalidUserInputExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("Tests for the method play()")
    class Play {

        @Test
        @DisplayName("Testing of the play() method")
        void playTest() throws Exception {
            final var gameResponseDto = createGameResultDto();
            final var gameRequest = createGameRequest(ROCK);

            when(rpsService.play(any(GameRequest.class), any(UUID.class))).thenReturn(gameResponseDto);

            final var outputMessage = new MockHttpOutputMessage();
            protobufJsonFormatHttpMessageConverter.write(gameRequest, APPLICATION_JSON, outputMessage);
            final var json = outputMessage.getBodyAsString();

            assertNotNull(json);

            mockMvc.perform(post("/v2/games")
                    .contentType(APPLICATION_JSON)
                    .content(json)
                    .accept(APPLICATION_JSON)
                    .characterEncoding(UTF_8))
                    .andDo(print())
                    // response validation
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(APPLICATION_JSON));

            // verify that it was the only invocation and
            // that there's no more unverified interactions
            verify(authenticationService, only()).getUserId();
            verifyNoMoreInteractions(authenticationService);
            reset(authenticationService);

            verify(rpsService).play(gameRequestArgumentCaptor.capture(), userIdArgumentCaptor.capture());
            assertEquals(ROCK, gameRequestArgumentCaptor.getValue().getHand());
            verifyNoMoreInteractions(rpsService);
            reset(rpsService);
        }
    }

    @Nested
    @DisplayName("Tests for the method deleteById()")
    class DeleteById {

        @Test
        @DisplayName("Testing of the deleteById() method")
        void deleteByIdTest() throws Exception {
            mockMvc.perform(delete("/v2/admin/games/{id}", TEST_UUID)
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .characterEncoding(UTF_8))
                    .andDo(print())
                    // response validation
                    .andExpect(status().isNoContent());

            // verify that it was the only invocation and
            // that there's no more unverified interactions
            verify(authenticationService, only()).getUserId();
            verifyNoMoreInteractions(authenticationService);
            reset(authenticationService);

            verify(rpsService).deleteById(idArgumentCaptor.capture(), userIdArgumentCaptor.capture());
            assertEquals(TEST_UUID, idArgumentCaptor.getValue());
            verifyNoMoreInteractions(rpsService);
            reset(rpsService);
        }
    }
}
