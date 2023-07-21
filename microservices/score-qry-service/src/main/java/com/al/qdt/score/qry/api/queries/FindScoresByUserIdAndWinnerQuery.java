package com.al.qdt.score.qry.api.queries;

import com.al.qdt.cqrs.queries.BasePagination;
import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FindScoresByUserIdAndWinnerQuery extends BasePagination implements BaseQuery {
    public static final String USER_ID_MUST_NOT_BE_NULL = "User id must not be null or empty";
    public static final String WINNER_MUST_NOT_BE_BLANK = "Winner must not be null or empty";

    @NotNull(message = USER_ID_MUST_NOT_BE_NULL)
    UUID userId;

    @NotBlank(message = WINNER_MUST_NOT_BE_BLANK)
    String winner;
}
