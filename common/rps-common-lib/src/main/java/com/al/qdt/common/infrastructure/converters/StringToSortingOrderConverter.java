package com.al.qdt.common.infrastructure.converters;

import com.al.qdt.cqrs.queries.SortingOrder;
import org.springframework.core.convert.converter.Converter;

/**
 * SortingOrder enum converter.
 */
public class StringToSortingOrderConverter implements Converter<String, SortingOrder> {

    @Override
    public SortingOrder convert(String source) {
        try {
            return SortingOrder.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
