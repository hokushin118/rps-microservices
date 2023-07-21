package com.al.qdt.score.qry.api.dto;

import com.al.qdt.common.api.dto.PagingDto;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class ScorePagedResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    Iterable<ScoreDto> scores;

    PagingDto pagination;
}
