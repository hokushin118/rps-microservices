package com.al.qdt.common.infrastructure.mongo.converters;

import com.al.qdt.common.domain.base.DateTimeConverterBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testing ZonedDateTimeReadConverter class")
class ZonedDateTimeReadConverterTest extends DateTimeConverterBaseTest {
    final ZonedDateTimeReadConverter zonedDateTimeReadConverter = new ZonedDateTimeReadConverter();

    @Test
    @DisplayName("Testing convert() method")
    void convertTest() {
        final var actualZonedDateTime = this.zonedDateTimeReadConverter.convert(expectedDate);

        assertNotNull(actualZonedDateTime);
        assertThat(actualZonedDateTime).isInstanceOf(ZonedDateTime.class);
        assertEquals(YEAR, actualZonedDateTime.getYear());
        assertEquals(MONTH, actualZonedDateTime.getMonth().getValue());
        assertEquals(DAY, actualZonedDateTime.getDayOfMonth());
        assertEquals(HOUR, actualZonedDateTime.getHour());
        assertEquals(MINUTE, actualZonedDateTime.getMinute());
        assertEquals(SECOND, actualZonedDateTime.getSecond());
        assertEquals(ZONE_OFFSET, actualZonedDateTime.getOffset().getTotalSeconds());
    }
}
