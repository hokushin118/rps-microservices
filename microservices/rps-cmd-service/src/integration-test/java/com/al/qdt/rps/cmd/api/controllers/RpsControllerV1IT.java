package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.rps.cmd.base.AbstractWebIntegrationTests;
import com.al.qdt.rps.cmd.base.DtoTests;
import com.al.qdt.rps.cmd.domain.services.RpsServiceV1;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import java.util.List;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_USER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RpsControllerV1.class)
@DisplayName("Integration testing of the RpsControllerV1 controller with partial context loading")
@Tag(value = "controller")
class RpsControllerV1IT extends AbstractWebIntegrationTests implements DtoTests {

    @MockBean
    RpsServiceV1 rpsService;

    Hand expectedHand;

    @BeforeEach
    public void setUp() {
        this.expectedHand = ROCK;
    }

    @AfterEach
    void clean() {
        this.expectedHand = null;
    }

    @Nested
    @DisplayName("Tests for the method play()")
    class Play {

        @Test
        @DisplayName("Testing of the play() method")
        void playTest() throws Exception {
            final var gameResponseDto = createGameResponseDto();

            when(rpsService.play(any(Hand.class), any()))
                    .thenReturn(gameResponseDto);

            final var json = objectMapper.writeValueAsString(expectedHand);

            assertNotNull(json);

            mockMvc.perform(post("/v1/games")
                    .with(jwt()
                            .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                            .jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, TEST_USER)))
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
