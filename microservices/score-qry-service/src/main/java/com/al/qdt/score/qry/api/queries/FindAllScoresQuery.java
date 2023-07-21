package com.al.qdt.score.qry.api.queries;

import com.al.qdt.cqrs.queries.BasePagination;
import com.al.qdt.cqrs.queries.BaseQuery;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class FindAllScoresQuery extends BasePagination implements BaseQuery {
}
