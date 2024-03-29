package com.al.qdt.rps.qry.domain.mappers;

import com.al.qdt.common.infrastructure.events.rps.GamePlayedEvent;
import com.al.qdt.common.domain.mappers.CommonConfig;
import com.al.qdt.rps.qry.domain.entities.Game;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonConfig.class,
        builder = @Builder(disableBuilder = true))
public abstract class GameMapper {

    /**
     * Converts game played event {@link GamePlayedEvent} to game entity class {@link Game}.
     *
     * @param gamePlayedEvent game played event
     * @return game entity
     */
    public abstract Game toEntity(GamePlayedEvent gamePlayedEvent);

    @BeforeMapping
    protected void mapBaseEntityId(GamePlayedEvent gamePlayedEvent, @MappingTarget Game entity) {
        entity.setId(gamePlayedEvent.getId());
    }
}
