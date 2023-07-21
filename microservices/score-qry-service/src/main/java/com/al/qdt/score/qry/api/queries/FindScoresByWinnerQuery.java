package com.al.qdt.score.qry.api.queries;

import com.al.qdt.cqrs.queries.BasePagination;
import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FindScoresByWinnerQuery extends BasePagination implements BaseQuery {
    public static final String WINNER_MUST_NOT_BE_BLANK = "Winner must not be null or empty";

    @NotBlank(message = WINNER_MUST_NOT_BE_BLANK)
    String winner;
}
