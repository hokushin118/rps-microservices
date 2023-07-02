package com.al.qdt.score.qry.infrastructure.handlers;

import com.al.qdt.common.infrastructure.events.rps.GameDeletedEvent;
import com.al.qdt.common.infrastructure.events.score.ScoresAddedEvent;
import com.al.qdt.common.infrastructure.events.score.ScoresDeletedEvent;

public interface EventHandler {
    void on(ScoresAddedEvent event);

    void on(ScoresDeletedEvent event);

    void on(GameDeletedEvent event);
}
