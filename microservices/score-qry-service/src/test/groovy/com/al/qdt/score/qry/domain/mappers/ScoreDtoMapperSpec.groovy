package com.al.qdt.score.qry.domain.mappers

import com.al.qdt.score.qry.base.EntityTests
import org.mapstruct.factory.Mappers
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID

@Title("Testing of the ScoreDtoMapper interface")
class ScoreDtoMapperSpec extends Specification implements EntityTests {

    @Subject
    def mapper = Mappers.getMapper ScoreDtoMapper.class

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
