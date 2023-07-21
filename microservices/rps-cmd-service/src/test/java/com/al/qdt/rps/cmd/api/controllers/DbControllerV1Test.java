package com.al.qdt.rps.cmd.api.controllers;

import com.al.qdt.common.api.advices.GlobalRestExceptionHandler;
import com.al.qdt.rps.cmd.api.advices.InvalidUserInputExceptionHandler;
import com.al.qdt.rps.cmd.api.dto.BaseResponseDto;
import com.al.qdt.rps.cmd.base.DtoTests;
import com.al.qdt.rps.cmd.domain.services.DbServiceV1;
import com.al.qdt.rps.cmd.domain.services.security.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing of the DbControllerV1 class")
@Tag(value = "controller")
class DbControllerV1Test implements DtoTests {

    @Mock
    DbServiceV1 dbService;

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    DbControllerV1 restoreReadDbController;

    MockMvc mockMvc;
    BaseResponseDto baseResponse;

    @BeforeEach
    void setUp() {
        this.baseResponse = createBaseResponseDto();

        lenient().when(this.authenticationService.getUserId()).thenReturn(UUID.randomUUID());

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.restoreReadDbController)
                .addPlaceholderValue("api.version-one", "/v1")
                .addPlaceholderValue("api.version-one-async", "/v1.1")
                .addPlaceholderValue("api.endpoint-db", "databases")
                .addPlaceholderValue("api.endpoint-db-restore", "restore")
                .addPlaceholderValue("api.endpoint-admin", "admin")
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setControllerAdvice(new InvalidUserInputExceptionHandler())
                .build();
    }

    @AfterEach
    void clean() {
        this.baseResponse = null;
    }

    @Test
    @DisplayName("Testing of the restoreReadDb() method")
    void restoreReadDbTest() throws Exception {
        when(this.dbService.restoreDb(any(UUID.class))).thenReturn(baseResponse);

        this.mockMvc.perform(post("/v1/admin/databases/restore")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(print())
                // response validation
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));

        // verify that it was the only invocation and
        // that there's no more unverified interactions
        verify(this.dbService, only()).restoreDb(any(UUID.class));
        verifyNoMoreInteractions(this.dbService);
        reset(this.dbService);
    }

    @Test
    @DisplayName("Testing of the restoreReadDbAsync() method")
    void restoreReadDbAsyncTest() throws Exception {
        final var baseResponseCompletableFuture = CompletableFuture.completedFuture(baseResponse);

        when(this.dbService.restoreDbAsync(any(UUID.class))).thenReturn(baseResponseCompletableFuture);

        final var mvcResult = mockMvc.perform(post("/v1.1/admin/databases/restore")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andExpect(request().asyncStarted())
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                // response validation
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));

        // verify that it was the only invocation and
        // that there's no more unverified interactions
        verify(authenticationService, only()).getUserId();
        verifyNoMoreInteractions(authenticationService);
        reset(authenticationService);

        verify(this.dbService, only()).restoreDbAsync(any(UUID.class));
        verifyNoMoreInteractions(this.dbService);
        reset(this.dbService);
    }
}
