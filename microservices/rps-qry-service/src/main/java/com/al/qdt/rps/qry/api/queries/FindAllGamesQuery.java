package com.al.qdt.rps.qry.api.queries;

import com.al.qdt.cqrs.queries.BasePagination;
import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FindAllGamesQuery extends BasePagination implements BaseQuery {
}
