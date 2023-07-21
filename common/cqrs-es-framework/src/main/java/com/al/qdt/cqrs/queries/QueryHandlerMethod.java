package com.al.qdt.cqrs.queries;

import com.al.qdt.cqrs.domain.AbstractEntity;

import java.util.AbstractMap;
import java.util.List;

public interface QueryHandlerMethod<T extends BaseQuery> {
    AbstractMap.SimpleImmutableEntry<Long, List<AbstractEntity>> handle(T query);
}
