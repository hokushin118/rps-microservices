package com.al.qdt.common.infrastructure.converters;

import com.al.qdt.rps.grpc.v1.common.SortingOrder;
import org.springframework.core.convert.converter.Converter;

/**
 * SortingOrder proto enum converter.
 */
public class StringToSortingOrderProtoConverter implements Converter<String, SortingOrder> {

    @Override
    public SortingOrder convert(String source) {
        try {
            return SortingOrder.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
