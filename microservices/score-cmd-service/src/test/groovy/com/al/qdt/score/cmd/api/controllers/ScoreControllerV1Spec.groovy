package com.al.qdt.score.cmd.api.controllers

import com.al.qdt.common.api.advices.GlobalRestExceptionHandler
import com.al.qdt.score.cmd.domain.services.ScoreServiceV1
import com.al.qdt.score.cmd.domain.services.security.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Title("Testing of the ScoreControllerV1 class")
class ScoreControllerV1Spec extends Specification {

    @Shared
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter

    @Subject
    def scoreService = Mock ScoreServiceV1

    @Subject
    def authenticationService = Mock AuthenticationService

    MockMvc mockMvc

    // Run before the first feature method
    def setupSpec() {
        mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter()
        mappingJackson2HttpMessageConverter.setObjectMapper(new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE) )
    }

    // Run before every feature method
    def setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ScoreControllerV1(scoreService, authenticationService))
                .addPlaceholderValue("api.version-one", "/v1")
                .addPlaceholderValue("api.endpoint-scores", "scores")
                .addPlaceholderValue("api.endpoint-admin", "admin")
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build()

        assert mockMvc
    }

    def 'Testing of the deleteById() method'() {
        when: 'Calling the api'
        def result = mockMvc.perform(delete("/v1/admin/scores/{id}", TEST_UUID)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(print())

        then: 'Status validation'
        result?.andExpect status().isNoContent()
    }
}
