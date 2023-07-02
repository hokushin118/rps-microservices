package com.al.qdt.common.infrastructure.events.rps;

import com.al.qdt.common.domain.enums.Hand;
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
public class GamePlayedEvent extends BaseEvent {

    @EqualsAndHashCode.Exclude
    @NotNull
    Hand hand;

    @Builder
    public GamePlayedEvent(@JsonProperty("id") UUID id, @JsonProperty("user_id") UUID userId, @JsonProperty("hand") Hand hand) {
        super(id, userId);
        this.hand = hand;
    }
}
