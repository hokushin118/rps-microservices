package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.api.dto.BaseResponseDto;
import com.al.qdt.rps.cmd.base.AbstractWebIntegrationTests;
import com.al.qdt.rps.cmd.base.DtoTests;
import com.al.qdt.rps.cmd.domain.services.AdminServiceV1;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestoreDbControllerV1.class)
@DisplayName("Integration testing of the RestoreDbControllerV1 controller with partial context loading")
@Tag(value = "controller")
class RestoreDbControllerV1IT extends AbstractWebIntegrationTests implements DtoTests {

    @MockBean
    AdminServiceV1 adminService;

    BaseResponseDto baseResponse;

    @BeforeEach
    void setUp() {
        this.baseResponse = createBaseResponse();
    }

    @AfterEach
    void clean() {
        this.baseResponse = null;
    }

    @SneakyThrows({JsonProcessingException.class, Exception.class})
    @Test
    @DisplayName("Testing of the restoreReadDb() method")
    void restoreReadDbTest() {
        when(this.adminService.restoreDb())
                .thenReturn(this.baseResponse);

        mockMvc.perform(post("/v1/admin")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(print())
                // response validation
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value(this.baseResponse.getMessage()));
    }

    @SneakyThrows({JsonProcessingException.class, Exception.class})
    @Test
    @DisplayName("Testing of the restoreReadDbAsync() method")
    void restoreReadDbAsyncTest() {
        when(this.adminService.restoreDbAsync())
                .thenReturn(CompletableFuture.supplyAsync(() -> this.baseResponse));

        final var mvcResult = mockMvc.perform(post("/v1.1/admin")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                // response validation
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value(this.baseResponse.getMessage()));
    }
}
