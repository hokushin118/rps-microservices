package com.al.qdt.score.qry.domain.mappers;

import com.al.qdt.common.infrastructure.events.score.ScoresAddedEvent;
import com.al.qdt.common.domain.mappers.CommonConfig;
import com.al.qdt.score.qry.domain.entities.Score;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonConfig.class,
        builder = @Builder(disableBuilder = true))
public abstract class ScoreMapper {

    /**
     * Converts scores added event {@link ScoresAddedEvent} to score entity class {@link Score}.
     *
     * @param scoresAddedEvent scores added event
     * @return score entity
     */
    public abstract Score toEntity(ScoresAddedEvent scoresAddedEvent);

    @BeforeMapping
    protected void mapBaseEntityId(ScoresAddedEvent scoresAddedEvent, @MappingTarget Score entity) {
        entity.setId(scoresAddedEvent.getId());
    }
}
