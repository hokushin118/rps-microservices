package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.api.dto.GameDto;
import com.al.qdt.rps.cmd.base.AbstractWebIntegrationTests;
import com.al.qdt.rps.cmd.base.DtoTests;
import com.al.qdt.rps.cmd.domain.services.RpsServiceV1;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.al.qdt.common.enums.Hand.ROCK;
import static com.al.qdt.common.helpers.Constants.USERNAME_ONE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RpsControllerV1.class)
@DisplayName("Integration testing of the RpsControllerV1 controller with partial context loading")
@Tag(value = "controller")
class RpsControllerV1IT extends AbstractWebIntegrationTests implements DtoTests {

    @MockBean
    RpsServiceV1 rpsService;

    GameDto expectedGameDto;

    @BeforeEach
    public void setUp() {
        this.expectedGameDto = createGameDto(USERNAME_ONE);
    }

    @AfterEach
    void clean() {
        this.expectedGameDto = null;
    }

    @Nested
    @DisplayName("Tests for the method play()")
    class Play {

        @SneakyThrows({JsonProcessingException.class, Exception.class})
        @Test
        @DisplayName("Testing of the play() method")
        void playTest() {
            final var gameResponseDto = createGameResponseDto();

            when(rpsService.play(any(GameDto.class)))
                    .thenReturn(gameResponseDto);

            final var json = objectMapper.writeValueAsString(expectedGameDto);

            assertNotNull(json);

            mockMvc.perform(post("/v1/games")
                    .contentType(APPLICATION_JSON)
                    .content(json)
                    .accept(APPLICATION_JSON)
                    .characterEncoding(UTF_8))
                    .andDo(print())
                    // response validation
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.user_choice")
                            .exists())
                    .andExpect(jsonPath("$.user_choice")
                            .value(ROCK.name()))
                    .andExpect(jsonPath("$.machine_choice")
                            .exists())
                    .andExpect(jsonPath("$.result")
                            .exists());

        }
    }

    @Disabled
    @Nested
    @DisplayName("Tests for the method deleteById()")
    class DeleteById {

    }
}
