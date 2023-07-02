package com.al.qdt.common.infrastructure.events.score;

import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.cqrs.events.BaseEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@Jacksonized
@EqualsAndHashCode(callSuper = false)
@ToString
public class ScoresAddedEvent extends BaseEvent {

    @EqualsAndHashCode.Exclude
    @NotNull
    Player winner;

    @Builder
    public ScoresAddedEvent(@JsonProperty("id") UUID id, @JsonProperty("user_id") UUID userId, @JsonProperty("winner") Player winner) {
        super(id, userId);
        this.winner = winner;
    }
}
