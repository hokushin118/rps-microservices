package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.api.advices.GlobalRestExceptionHandler;
import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.rps.cmd.api.advices.InvalidUserInputExceptionHandler;
import com.al.qdt.rps.cmd.base.DtoTests;
import com.al.qdt.rps.cmd.domain.services.RpsServiceV1;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Utils.createObjectMapper;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing of the RpsControllerV1 controller")
@Tag(value = "controller")
class RpsControllerV1Test implements DtoTests {

    @Mock
    RpsServiceV1 rpsService;

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    RpsControllerV1 rpsController;

    @Captor
    ArgumentCaptor<Hand> gameDtoArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> idArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> userIdArgumentCaptor;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = createObjectMapper();
        this.gameDtoArgumentCaptor = ArgumentCaptor.forClass(Hand.class);
        this.idArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        lenient().when(this.authenticationService.getUserId()).thenReturn(UUID.randomUUID());

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.rpsController)
                .addPlaceholderValue("api.version-one", "/v1")
                .addPlaceholderValue("api.version-one-async", "/v1.1")
                .addPlaceholderValue("api.endpoint-games", "games")
                .addPlaceholderValue("api.endpoint-admin", "admin")
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
            final var gameResponseDto = createGameResponseDto();
            final var hand = ROCK;

            when(rpsService.play(any(Hand.class), any(UUID.class))).thenReturn(gameResponseDto);

            final var json = objectMapper.writeValueAsString(hand);

            assertNotNull(json);

            mockMvc.perform(post("/v1/games")
                    .contentType(APPLICATION_JSON)
                    .content(json)
                    .accept(APPLICATION_JSON_VALUE)
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

            verify(rpsService, only()).play(gameDtoArgumentCaptor.capture(), userIdArgumentCaptor.capture());
            assertEquals(ROCK, gameDtoArgumentCaptor.getValue());
            verifyNoMoreInteractions(rpsService);
            reset(rpsService);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Testing of the play() method with null or empty hand, throws validation exception")
        void playInvalidParamUsernameNullOrEmptyTest(String hand) throws Exception {
            final var json = objectMapper.writeValueAsString(hand);

            assertNotNull(json);

            mockMvc.perform(post("/v1/games")
                    .contentType(APPLICATION_JSON)
                    .content(json)
                    .accept(APPLICATION_JSON_VALUE)
                    .characterEncoding(UTF_8))
                    .andDo(print())
                    // response validation
                    .andExpect(status().isBadRequest());

            // verify that it was no invocation
            verify(authenticationService, never()).getUserId();

            // verify that it was the only invocation and
            // that there's no more unverified interactions
            verify(rpsService, never()).play(any(Hand.class), any(UUID.class));
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
            mockMvc.perform(delete("/v1/admin/games/{id}", TEST_UUID)
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

            verify(rpsService, only()).deleteById(idArgumentCaptor.capture(), userIdArgumentCaptor.capture());
            assertEquals(TEST_UUID, idArgumentCaptor.getValue());
            verifyNoMoreInteractions(rpsService);
            reset(rpsService);
        }
    }
}
