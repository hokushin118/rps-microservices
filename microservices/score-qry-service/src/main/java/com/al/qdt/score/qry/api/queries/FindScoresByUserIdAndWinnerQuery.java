package com.al.qdt.score.qry.api.queries;

import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@AllArgsConstructor
public class FindScoresByUserIdAndWinnerQuery implements BaseQuery {
    public static final String USER_ID_MUST_NOT_BE_NULL = "User id must not be null or empty";
    public static final String WINNER_MUST_NOT_BE_BLANK = "Winner must not be null or empty";

    @NotNull(message = USER_ID_MUST_NOT_BE_NULL)
    UUID userId;

    @NotBlank(message = WINNER_MUST_NOT_BE_BLANK)
    String winner;
}
