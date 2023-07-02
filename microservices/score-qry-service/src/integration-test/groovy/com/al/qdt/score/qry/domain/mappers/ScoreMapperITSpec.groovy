package com.al.qdt.score.qry.domain.mappers

import com.al.qdt.score.qry.base.AbstractIntegrationTests
import com.al.qdt.score.qry.base.EventTests
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject
import spock.lang.Title

import static com.al.qdt.common.domain.enums.Player.USER
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID

@Title("Integration testing of the ScoreMapper class")
class ScoreMapperITSpec extends AbstractIntegrationTests implements EventTests {

    @Subject
    @Autowired
    ScoreMapper mapper

    def 'Testing injections'() {
        expect:
        assert mapper
    }

    def 'Testing of the toEntity() method'() {
        given: 'Setup test data'
        def scoresAddedEvent = createScoresAddedEvent()

        when: 'Mapped to entity object'
        def score = mapper.toEntity scoresAddedEvent

        then: 'Mapped successfully'
        assert score
        assert score.id == TEST_UUID && score.id == scoresAddedEvent.id
        assert score.userId == USER_ONE_ID && score.userId == scoresAddedEvent.userId
        assert score.winner == USER && score.winner == scoresAddedEvent.winner
    }
}
