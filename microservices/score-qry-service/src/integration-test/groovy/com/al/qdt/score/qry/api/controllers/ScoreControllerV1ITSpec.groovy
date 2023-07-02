package com.al.qdt.score.qry.api.controllers

import com.al.qdt.score.qry.base.AbstractIntegrationTests
import com.al.qdt.score.qry.base.MvcHelper
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import spock.lang.Title

import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@Title("Integration testing of the ScoreControllerV1 class")
class ScoreControllerV1ITSpec extends AbstractIntegrationTests implements MvcHelper {

    def 'Testing of the all() method'() {
        when: 'Calling the api'
        def result = mockMvc.perform(get("/v1/admin/scores")
                .with(jwt().jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, TEST_USER)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo print()

        then: 'Status and content type validation'
        testStatusAndContentType(result)

        and: 'Etag availability'
        result?.andExpect header().exists('Etag')

        and: 'Response validation'
        result?.andExpect jsonPath('$.[0].id').exists()
        result?.andExpect jsonPath('$.[0].id').value(expectedScore.id.toString())
        result?.andExpect jsonPath('$.[0].user_id').exists()
        result?.andExpect jsonPath('$.[0].user_id').value(expectedScore.userId.toString())
        result?.andExpect jsonPath('$.[0].winner').exists()
        result?.andExpect jsonPath('$.[0].winner').value(expectedScore.winner.name())
    }

    def 'Testing of the findById() method'() {
        when: 'Calling the api'
        def result = mockMvc.perform(get("/v1/admin/scores/{id}", TEST_UUID)
                .with(jwt().jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, TEST_USER)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo(print())

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Etag availability'
        result?.andExpect header().exists('Etag')

        and: 'Response validation'
        result?.andExpect jsonPath('$.id').exists()
        result?.andExpect jsonPath('$.id').value(expectedScore.id.toString())
        result?.andExpect jsonPath('$.[0].user_id').exists()
        result?.andExpect jsonPath('$.[0].user_id').value(expectedScore.userId.toString())
        result?.andExpect jsonPath('$.winner').exists()
        result?.andExpect jsonPath('$.winner').value(expectedScore.winner.name())
    }

    def 'Testing of the findMyScores() method'() {
        when: 'Calling the api'
        def result = mockMvc.perform(get("/v1/scores")
                .with(jwt().jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, TEST_USER)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON_VALUE)
                .characterEncoding(UTF_8))
                .andDo(print())

        then: 'Status and content type validation'
        testStatusAndContentType result

        and: 'Etag availability'
        result?.andExpect header().exists('Etag')

        and: 'Response validation'
        result?.andExpect jsonPath('$.[0].id').exists()
        result?.andExpect jsonPath('$.[0].id').value(expectedScore.id.toString())
        result?.andExpect jsonPath('$.[0].winner').exists()
        result?.andExpect jsonPath('$.[0].winner').value(expectedScore.winner.name())
    }
}
