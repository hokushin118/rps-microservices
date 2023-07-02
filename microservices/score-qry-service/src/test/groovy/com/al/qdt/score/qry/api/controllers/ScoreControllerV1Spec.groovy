package com.al.qdt.score.qry.api.controllers

import com.al.qdt.common.api.advices.GlobalRestExceptionHandler
import com.al.qdt.common.api.dto.ScoreAdminDto
import com.al.qdt.common.api.dto.ScoreDto
import com.al.qdt.score.qry.base.MvcHelper
import com.al.qdt.score.qry.domain.services.ScoreServiceV1
import com.al.qdt.score.qry.domain.services.security.AuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.domain.enums.Player.MACHINE
import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_TWO_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_TWO_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@Title("Testing of the ScoreControllerV1 class")
class ScoreControllerV1Spec extends Specification implements MvcHelper {

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
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build()

        assert mockMvc
    }

    def 'Testing of the all() method'() {
        given: 'Setup test data'
        def firstScoreAdminDto = ScoreAdminDto.builder()
                .id(TEST_ID)
                .userId(USER_ONE_ID.toString())
                .winner(USER.name())
                .build()
        def secondScoreAdminDto = ScoreAdminDto.builder()
                .id(TEST_TWO_ID)
                .userId(USER_TWO_ID.toString())
                .winner(MACHINE.name())
                .build()

        and: 'Mock returns list of scores for admin users if invoked with no argument'
        scoreService.all() >> [firstScoreAdminDto, secondScoreAdminDto]

        when: 'Calling the api'
        def result = mockMvc.perform(get("/v1/admin/scores")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo print()

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Response validation'
        result?.andExpect jsonPath('$.[0].id').exists()
        result?.andExpect jsonPath('$.[0].id').value(firstScoreAdminDto.id.toString())
        result?.andExpect jsonPath('$.[1].id').exists()
        result?.andExpect jsonPath('$.[1].id').value(secondScoreAdminDto.id.toString())
        result?.andExpect jsonPath('$.[0].user_id').exists()
        result?.andExpect jsonPath('$.[0].user_id').value(firstScoreAdminDto.userId.toString())
        result?.andExpect jsonPath('$.[1].user_id').exists()
        result?.andExpect jsonPath('$.[1].user_id').value(secondScoreAdminDto.userId.toString())
        result?.andExpect jsonPath('$.[0].winner').exists()
        result?.andExpect jsonPath('$.[0].winner').value(firstScoreAdminDto.winner)
        result?.andExpect jsonPath('$.[1].winner').exists()
        result?.andExpect jsonPath('$.[1].winner').value(secondScoreAdminDto.winner)
    }

    def 'Testing of the findById() method'() {
        given: 'Setup test data'
        def expectedScoreAdminDto = ScoreAdminDto.builder()
                .id(TEST_ID)
                .userId(USER_ONE_ID.toString())
                .winner(USER.name())
                .build()

        and: 'Mock returns score if invoked with id argument'
        scoreService.findById(TEST_UUID) >> expectedScoreAdminDto

        when: 'Calling the api'
        def result = mockMvc.perform(get("/v1/admin/scores/{id}", TEST_UUID)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo print()

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Response validation'
        result?.andExpect jsonPath('$.id').exists()
        result?.andExpect jsonPath('$.id').value(expectedScoreAdminDto.id.toString())
        result?.andExpect jsonPath('$.user_id').exists()
        result?.andExpect jsonPath('$.user_id').value(expectedScoreAdminDto.userId.toString())
        result?.andExpect jsonPath('$.winner').exists()
        result?.andExpect jsonPath('$.winner').value(expectedScoreAdminDto.winner)
    }

    def 'Testing of the findMyScores() method'() {
        given: 'Setup test data'
        def userId = UUID.randomUUID()

        def expectedScore = ScoreDto.builder()
                .id(TEST_ID)
                .winner(USER.name())
                .build()

        and: 'Mock returns userId if invoked'
        authenticationService.getUserId() >> userId

        and: 'Mock returns list of user scores if invoked with userId argument'
        scoreService.findMyScores(userId) >> [expectedScore]

        when: 'Calling the api'
        def result = mockMvc.perform(get("/v1/scores")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo print()

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Response validation'
        result?.andExpect jsonPath('$.[0].id').exists()
        result?.andExpect jsonPath('$.[0].id').value(expectedScore.id.toString())
        result?.andExpect jsonPath('$.[0].winner').exists()
        result?.andExpect jsonPath('$.[0].winner').value(expectedScore.winner)
    }
}
