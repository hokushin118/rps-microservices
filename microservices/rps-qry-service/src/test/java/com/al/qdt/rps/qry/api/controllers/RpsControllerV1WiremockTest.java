package com.al.qdt.rps.qry.api.controllers;

import com.al.qdt.common.api.advices.GlobalRestExceptionHandler;
import com.al.qdt.rps.qry.base.DtoTests;
import com.al.qdt.rps.qry.domain.services.RpsServiceV1;
import com.al.qdt.rps.qry.domain.services.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_TWO_ID;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing of the RpsControllerV1 class")
@Tag(value = "controller")
class RpsControllerV1WiremockTest implements DtoTests {
    MockMvc mockMvc;

    @Mock
    RpsServiceV1 rpsService;

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    RpsControllerV1 rpsController;

    @Captor
    ArgumentCaptor<UUID> idParamArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> userIdParamArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.idParamArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        this.userIdParamArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        lenient().when(this.authenticationService.getUserId()).thenReturn(UUID.randomUUID());

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.rpsController)
                .addPlaceholderValue("api.version-one", "/v1")
                .addPlaceholderValue("api.endpoint-games", "games")
                .addPlaceholderValue("api.endpoint-admin", "admin")
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Testing of the all() method")
    void allTest() throws Exception {
        final var firstGameAdminDto = createGameAdminDto(USER_ONE_ID);
        final var secondGameAdminDto = createGameAdminDto(USER_TWO_ID);

        when(this.rpsService.all()).thenReturn(List.of(firstGameAdminDto, secondGameAdminDto));

        this.mockMvc.perform(get("/v1/admin/games")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo(print())
                // response validation
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));

        // verify that it was the only invocation and
        // that there's no more unverified interactions
        verify(this.rpsService, only()).all();
        reset(this.rpsService);
    }

    @Test
    @DisplayName("Testing of the findById() method")
    void findByIdTest() throws Exception {
        final var gameAdminDto = createGameAdminDto(USER_ONE_ID);

        when(this.rpsService.findById(any(UUID.class))).thenReturn(gameAdminDto);

        this.mockMvc.perform(get("/v1/admin/games/{id}", TEST_UUID)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo(print())
                // response validation
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));

        // verify that it was the only invocation and
        // that there's no more unverified interactions
        verify(this.rpsService, only()).findById(this.idParamArgumentCaptor.capture());
        assertEquals(TEST_UUID, this.idParamArgumentCaptor.getValue());
        reset(this.rpsService);
    }

    @Test
    @DisplayName("Testing of the findMyGames() method")
    void findMyGamesTest() throws Exception {
        final var gameDto = createGameDto(USER_ONE_ID);

        when(this.rpsService.findMyGames(any(UUID.class))).thenReturn(List.of(gameDto));

        this.mockMvc.perform(get("/v1/games")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo(print())
                // response validation
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));

        // verify that it was the only invocation and
        // that there's no more unverified interactions
        verify(this.rpsService, only()).findMyGames(this.userIdParamArgumentCaptor.capture());
        reset(this.rpsService);
    }
}
