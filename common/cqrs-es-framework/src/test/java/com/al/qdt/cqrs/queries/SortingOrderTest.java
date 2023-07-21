package com.al.qdt.cqrs.queries;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.al.qdt.cqrs.queries.SortingOrder.ASC;
import static com.al.qdt.cqrs.queries.SortingOrder.DESC;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing SortingOrder enum")
class SortingOrderTest {

    private static final int TOTAL_ITEMS = 2; // total number of items defined in enum

    @Test
    void valuesTest() {
        assertEquals(TOTAL_ITEMS, SortingOrder.values().length);
    }

    @Test
    @DisplayName("Testing valueOf() method")
    void valueOfTest() {
        assertEquals(ASC, SortingOrder.valueOf("ASC"));
        assertEquals(DESC, SortingOrder.valueOf("DESC"));
    }
}
