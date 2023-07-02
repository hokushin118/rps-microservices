package com.al.qdt.rps.cmd.api.commands;

import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.cqrs.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Value
@SuperBuilder
@AllArgsConstructor
public class PlayGameCommand extends BaseCommand {
    public static final String HAND_MUST_NOT_BE_NULL = "Hand must not be null";

    @NotNull(message = HAND_MUST_NOT_BE_NULL)
    Hand hand;
}
