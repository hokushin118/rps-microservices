package com.al.qdt.rps.cmd.domain.services.engine;

import com.al.qdt.common.domain.enums.Hand;
import com.al.qdt.common.domain.enums.Player;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RoundResult {
    Hand machineChoice;
    Player winner;
}
