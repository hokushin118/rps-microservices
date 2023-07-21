package com.al.qdt.score.qry.api.controllers

import com.al.qdt.common.api.advices.GlobalRestExceptionHandler
import com.al.qdt.rps.grpc.v1.common.SortingOrder
import com.al.qdt.rps.grpc.v1.services.ListOfScoresAdminResponse
import com.al.qdt.rps.grpc.v1.services.ListOfScoresResponse
import com.al.qdt.score.qry.base.MvcHelper
import com.al.qdt.score.qry.base.ProtoTests
import com.al.qdt.score.qry.domain.services.ScoreServiceV2
import com.al.qdt.score.qry.domain.services.security.AuthenticationService
import com.google.protobuf.util.JsonFormat
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.rps.grpc.v1.common.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID_TWO
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_TWO_ID
import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@Title("Testing of the ScoreControllerV2 class")
class ScoreControllerV2Spec extends Specification implements ProtoTests, MvcHelper {

    @Shared
    ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter

    @Subject
    def scoreService = Mock ScoreServiceV2

    @Subject
    def authenticationService = Mock AuthenticationService

    MockMvc mockMvc

    // Run before the first feature method
    def setupSpec() {
        def parser = JsonFormat.parser()
                .ignoringUnknownFields()
        def printer = JsonFormat.printer()
                .includingDefaultValueFields()
        protobufJsonFormatHttpMessageConverter = new ProtobufJsonFormatHttpMessageConverter(parser, printer)
    }

    // Run before every feature method
    def setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ScoreControllerV2(scoreService, authenticationService))
                .addPlaceholderValue("api.version-two", "/v2")
                .addPlaceholderValue("api.endpoint-scores", "scores")
                .addPlaceholderValue("api.endpoint-admin", "admin")
                .addPlaceholderValue("api.default-page-number", "1")
                .addPlaceholderValue("api.default-page-size", "10")
                .addPlaceholderValue("api.default-sort-by", "id")
                .addPlaceholderValue("api.default-sort-order", SortingOrder.ASC.name())
                .setMessageConverters(protobufJsonFormatHttpMessageConverter)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build()

        assert mockMvc
    }

    def 'Testing of the all() method'() {
        given: 'Setup test data'
        def firstScoreAdminDto = createScoreAdminProtoDto TEST_UUID, USER_ONE_ID, USER
        def secondScoreAdminDto = createScoreAdminProtoDto TEST_UUID_TWO, USER_TWO_ID, USER
        def listOfScoresAdminResponse = ListOfScoresAdminResponse.newBuilder()
                .addAllScores([firstScoreAdminDto, secondScoreAdminDto])
                .build()

        and: 'Mock returns list of scores for admin users if invoked with no argument'
        scoreService.all(1, 10, "id", SortingOrder.ASC) >> listOfScoresAdminResponse

        when: 'Calling the api'
        def result = mockMvc.perform(get("/v2/admin/scores")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .param("currentPage", "1")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortingOrder", SortingOrder.ASC.name())
                .characterEncoding(UTF_8))
                .andDo print()

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Response validation'
        result?.andExpect jsonPath('$.scores').exists()
        result?.andExpect jsonPath('$.scores[0].id').exists()
        result?.andExpect jsonPath('$.scores[0].id').value(firstScoreAdminDto.id)
        result?.andExpect jsonPath('$.scores[1].id').exists()
        result?.andExpect jsonPath('$.scores[1].id').value(secondScoreAdminDto.id)
        result?.andExpect jsonPath('$.scores[0].user_id').exists()
        result?.andExpect jsonPath('$.scores[0].user_id').value(firstScoreAdminDto.userId)
        result?.andExpect jsonPath('$.scores[1].user_id').exists()
        result?.andExpect jsonPath('$.scores[1].user_id').value(secondScoreAdminDto.userId)
        result?.andExpect jsonPath('$.scores[0].winner').exists()
        result?.andExpect jsonPath('$.scores[0].winner').value(firstScoreAdminDto.winner)
        result?.andExpect jsonPath('$.scores[1].winner').exists()
        result?.andExpect jsonPath('$.scores[1].winner').value(secondScoreAdminDto.winner)
    }

    def 'Testing of the findById() method'() {
        given: 'Setup test data'
        def expectedScoreAdminDto = createScoreAdminProtoDto TEST_UUID, USER_ONE_ID, USER

        and: 'Mock returns a scores if invoked with id argument'
        scoreService.findById(TEST_UUID) >> expectedScoreAdminDto

        when: 'Calling the api'
        def result = mockMvc.perform(get("/v2/admin/scores/{id}", TEST_UUID)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo print()

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Response validation'
        result?.andExpect jsonPath('$.id').exists()
        result?.andExpect jsonPath('$.id').value(expectedScoreAdminDto.id)
        result?.andExpect jsonPath('$.user_id').exists()
        result?.andExpect jsonPath('$.user_id').value(expectedScoreAdminDto.userId)
        result?.andExpect jsonPath('$.winner').exists()
        result?.andExpect jsonPath('$.winner').value(expectedScoreAdminDto.winner)
    }

    def 'Testing of the findMyScores() method'() {
        given: 'Setup test data'
        def userId = UUID.randomUUID()
        def scoreDto = createScoreProtoDto()
        def listOfScoresResponse = ListOfScoresResponse.newBuilder()
                .addAllScores([scoreDto])
                .build()

        and: 'Mock returns userId if invoked'
        authenticationService.getUserId() >> userId

        and: 'Mock returns list of user scores if invoked with userId argument'
        scoreService.findMyScores(userId, 1, 10, "id", SortingOrder.ASC) >> listOfScoresResponse

        when: 'Calling the api'
        def result = mockMvc.perform(get("/v2/scores")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .param("currentPage", "1")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortingOrder", SortingOrder.ASC.name())
                .characterEncoding(UTF_8))
                .andDo print()

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Response validation'
        result?.andExpect jsonPath('$.scores').exists()
        result?.andExpect jsonPath('$.scores[0].id').exists()
        result?.andExpect jsonPath('$.scores[0].id').value(scoreDto.id)
        result?.andExpect jsonPath('$.scores[0].winner').exists()
        result?.andExpect jsonPath('$.scores[0].winner').value(scoreDto.winner)
    }
}
