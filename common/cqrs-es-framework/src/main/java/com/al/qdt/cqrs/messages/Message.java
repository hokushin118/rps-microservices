package com.al.qdt.cqrs.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Message {
    public static final String ID_MUST_NOT_BE_NULL = "Identification must not be null";
    public static final String USER_ID_MUST_NOT_BE_NULL = "User Id must not be null";

    @NotNull(message = ID_MUST_NOT_BE_NULL)
    private UUID id;

    @NotNull(message = USER_ID_MUST_NOT_BE_NULL)
    private UUID userId;
}
