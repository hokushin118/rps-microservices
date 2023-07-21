package com.al.qdt.common.infrastructure.helpers;

import com.al.qdt.common.infrastructure.serde.LocalDateDeserializer;
import com.al.qdt.common.infrastructure.serde.LocalDateSerializer;
import com.al.qdt.common.infrastructure.serde.LocalDateTimeDeserializer;
import com.al.qdt.common.infrastructure.serde.LocalDateTimeSerializer;
import com.al.qdt.cqrs.queries.SortingOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class Utils {

    /**
     * Creating sorting order.
     *
     * @param sortingBy    sorting field
     * @param sortingOrder sorting direction
     * @return sorting order
     */
    public static Sort getSortingOrder(String sortingBy, SortingOrder sortingOrder) {
        return sortingOrder.name().equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortingBy).ascending()
                : Sort.by(sortingBy).descending();
    }

    /**
     * Creating ObjectMapper instance.
     *
     * @return ObjectMapper instance
     */
    public static ObjectMapper createObjectMapper() {
        final var objectMapper = new ObjectMapper();

        final var javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(new ParameterNamesModule())
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper;
    }
}
