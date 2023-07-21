package com.al.qdt.cqrs.queries;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
public abstract class BasePagination {

    private int currentPage;
    private int pageSize;
    private String sortBy;
    private SortingOrder sortingOrder;
}
