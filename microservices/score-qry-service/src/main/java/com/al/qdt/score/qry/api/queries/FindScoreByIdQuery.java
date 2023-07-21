package com.al.qdt.score.qry.api.queries;

import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@SuperBuilder
public class FindScoreByIdQuery implements BaseQuery {
    public static final String ID_MUST_NOT_BE_NULL = "Identification must not be null";

    @NotNull(message = ID_MUST_NOT_BE_NULL)
    UUID id;
}
