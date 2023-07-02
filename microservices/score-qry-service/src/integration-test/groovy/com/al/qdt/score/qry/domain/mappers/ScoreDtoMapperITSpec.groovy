package com.al.qdt.score.qry.domain.mappers

import com.al.qdt.score.qry.base.AbstractIntegrationTests
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID

@Title("Integration testing of the ScoreDtoMapper interface")
class ScoreDtoMapperITSpec extends AbstractIntegrationTests {

    @Subject
    @Autowired
    ScoreDtoMapper mapper

    def 'Testing injections'() {
        expect:
        assert mapper
    }

    def 'Testing of the toScoreDto() method'() {
        given: 'Setup test data'
        def score = createScore TEST_UUID, USER_ONE_ID, USER

        when: 'Mapped to dto object'
        def scoreDto = mapper.toScoreDto score

        then: 'Mapped successfully'
        assert scoreDto
        assert scoreDto.id == TEST_UUID.toString() && scoreDto.id == score.id.toString()
        assert scoreDto.winner == USER.name() && scoreDto.winner == score.winner.name()
    }

    def 'Testing of the toScoreAdminDto() method'() {
        given: 'Setup test data'
        def score = createScore TEST_UUID, USER_ONE_ID, USER

        when: 'Mapped to dto object'
        def scoreAdminDto = mapper.toScoreAdminDto score

        then: 'Mapped successfully'
        assert scoreAdminDto
        assert scoreAdminDto.id == TEST_UUID.toString() && scoreAdminDto.id == score.id.toString()
        assert scoreAdminDto.userId == USER_ONE_ID.toString() && scoreAdminDto.userId == score.userId.toString()
        assert scoreAdminDto.winner == USER.name() && scoreAdminDto.winner == score.winner.name()
    }
}
