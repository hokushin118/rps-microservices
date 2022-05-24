package com.al.qdt.rps.qry.queries;

import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@AllArgsConstructor
public class FindGameByIdQuery implements BaseQuery {
    public static final String ID_MUST_NOT_BE_NULL = "Identification must not be null";

    @NotNull(message = ID_MUST_NOT_BE_NULL)
    UUID id;
}
