package com.al.qdt.rps.qry.infrastructure.handlers;

import com.al.qdt.cqrs.domain.AbstractEntity;
import com.al.qdt.rps.qry.api.queries.FindAllGamesQuery;
import com.al.qdt.rps.qry.api.queries.FindGameByIdQuery;
import com.al.qdt.rps.qry.api.queries.FindGamesByUserIdQuery;

import java.util.List;

public interface QueryHandler {
    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>>  handle(FindAllGamesQuery query);

    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindGameByIdQuery query);

    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(FindGamesByUserIdQuery query);
}
