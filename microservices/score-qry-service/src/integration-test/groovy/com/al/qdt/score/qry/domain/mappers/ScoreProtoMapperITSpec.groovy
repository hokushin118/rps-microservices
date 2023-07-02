package com.al.qdt.score.qry.domain.mappers

import com.al.qdt.score.qry.base.AbstractIntegrationTests
import com.al.qdt.score.qry.base.EntityTests
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID

@Title("Integration testing of the ScoreProtoMapper interface")
class ScoreProtoMapperITSpec extends AbstractIntegrationTests implements EntityTests {

    @Subject
    @Autowired
    ScoreProtoMapper mapper

    def 'Testing injections'() {
        expect:
        assert mapper
    }

    def 'Testing of the toStoreDto() method'() {
        given: 'Setup test data'
        def score = createScore TEST_UUID, USER_ONE_ID, USER

        when: 'Mapped to proto object'
        def scoreDto = mapper.toScoreDto score

        then: 'Mapped successfully'
        assert scoreDto
        assert scoreDto.id == TEST_UUID.toString()
        assert scoreDto.id == score.id.toString()
        assert scoreDto.winner == USER.name()
        assert scoreDto.winner == score.winner.name()
    }

    def 'Testing of the toStoreAdminDto() method'() {
        given: 'Setup test data'
        def score = createScore TEST_UUID, USER_ONE_ID, USER

        when: 'Mapped to proto object'
        def scoreAdminDto = mapper.toScoreAdminDto score

        then: 'Mapped successfully'
        assert scoreAdminDto
        assert scoreAdminDto.id == TEST_UUID.toString()
        assert scoreAdminDto.id == score.id.toString()
        assert scoreAdminDto.userId == USER_ONE_ID.toString()
        assert scoreAdminDto.userId == score.userId.toString()
        assert scoreAdminDto.winner == USER.name()
        assert scoreAdminDto.winner == score.winner.name()
    }
}
