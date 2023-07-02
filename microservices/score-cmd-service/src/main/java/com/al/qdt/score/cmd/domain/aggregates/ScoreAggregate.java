package com.al.qdt.score.cmd.domain.aggregates;

import com.al.qdt.score.cmd.api.commands.DeleteScoreCommand;
import com.al.qdt.common.infrastructure.events.score.ScoresDeletedEvent;
import com.al.qdt.cqrs.domain.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScoreAggregate extends AggregateRoot {

    public ScoreAggregate(DeleteScoreCommand command) {
        super.raiseEvent(ScoresDeletedEvent.builder()
                .id(command.getId())
                .userId(command.getUserId())
                .build());
    }

    public void apply(ScoresDeletedEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
    }
}
