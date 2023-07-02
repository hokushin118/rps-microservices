package com.al.qdt.score.qry.infrastructure.handlers;

import com.al.qdt.cqrs.domain.AbstractEntity;
import com.al.qdt.score.qry.api.queries.*;

import java.util.List;

public interface QueryHandler {
    List<AbstractEntity> handle(FindAllScoresQuery query);

    List<AbstractEntity> handle(FindScoreByIdQuery query);

    List<AbstractEntity> handle(FindScoresByUserIdQuery query);

    List<AbstractEntity> handle(FindScoresByWinnerQuery query);

    List<AbstractEntity> handle(FindScoresByUserIdAndWinnerQuery query);
}
