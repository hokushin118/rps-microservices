package com.al.qdt.common.infrastructure.events.rps;

import com.al.qdt.cqrs.events.BaseEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Jacksonized
@EqualsAndHashCode(callSuper = false)
@ToString
public class GameDeletedEvent extends BaseEvent {

    @Builder
    public GameDeletedEvent(@JsonProperty("id") UUID id, @JsonProperty("user_id") UUID userId) {
        super(id, userId);
    }
}
