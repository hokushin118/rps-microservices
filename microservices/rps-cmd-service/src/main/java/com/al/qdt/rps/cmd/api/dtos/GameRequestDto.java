package com.al.qdt.rps.cmd.api.dtos;

import com.al.qdt.common.domain.enums.Hand;
import lombok.Value;

@Value
public class GameRequestDto {
    Hand hand;
}
