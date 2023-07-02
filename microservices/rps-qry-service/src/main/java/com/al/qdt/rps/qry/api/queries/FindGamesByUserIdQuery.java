package com.al.qdt.rps.qry.api.queries;

import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@AllArgsConstructor
public class FindGamesByUserIdQuery implements BaseQuery {
    public static final String USER_ID_MUST_NOT_BE_NULL = "User id must not be null";

    @NotNull(message = USER_ID_MUST_NOT_BE_NULL)
    UUID userId;
}
