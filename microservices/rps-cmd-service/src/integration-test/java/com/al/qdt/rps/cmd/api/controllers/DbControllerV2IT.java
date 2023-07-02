package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.infrastructure.config.ProtoConfig;
import com.al.qdt.rps.cmd.base.AbstractWebIntegrationTests;
import com.al.qdt.rps.cmd.base.ProtoTests;
import com.al.qdt.rps.cmd.domain.services.DbServiceV2;
import com.al.qdt.rps.grpc.v1.common.BaseResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import java.util.concurrent.CompletableFuture;

import static com.al.qdt.common.infrastructure.helpers.Constants.ADMIN_USER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DbControllerV2.class)
@Import(ProtoConfig.class)
@DisplayName("Integration testing of the DbControllerV2 controller with partial context loading")
@Tag(value = "controller")
class DbControllerV2IT extends AbstractWebIntegrationTests implements ProtoTests {

    @MockBean
    DbServiceV2 dbService;

    BaseResponseDto baseResponse;

    @BeforeEach
    void setUp() {
        this.baseResponse = createBaseResponseDto();
    }

    @AfterEach
    void clean() {
        this.baseResponse = null;
    }

    @Test
    @DisplayName("Testing of the restoreReadDb() method")
    void restoreReadDbTest() throws Exception {
        when(this.dbService.restoreDb(any()))
                .thenReturn(this.baseResponse);

        mockMvc.perform(post("/v2/admin/databases/restore")
                .with(jwt().jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, ADMIN_USER)))
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

    @Test
    @DisplayName("Testing of the restoreReadDbAsync() method")
    void restoreReadDbAsyncTest() throws Exception {
        when(this.dbService.restoreDbAsync(any()))
                .thenReturn(CompletableFuture.supplyAsync(() -> this.baseResponse));

        final var mvcResult = mockMvc.perform(post("/v2.1/admin/databases/restore")
                .with(jwt().jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, ADMIN_USER)))
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
