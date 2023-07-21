package com.al.qdt.score.qry.infrastructure.handlers;

import com.al.qdt.cqrs.domain.AbstractEntity;
import com.al.qdt.score.qry.api.queries.FindAllScoresQuery;
import com.al.qdt.score.qry.api.queries.FindScoreByIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdAndWinnerQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByUserIdQuery;
import com.al.qdt.score.qry.api.queries.FindScoresByWinnerQuery;

import java.util.AbstractMap;
import java.util.List;

public interface QueryHandler {
    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindAllScoresQuery query);

    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoreByIdQuery query);

    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoresByUserIdQuery query);

    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoresByWinnerQuery query);

    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindScoresByUserIdAndWinnerQuery query);
}
