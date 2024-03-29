package com.al.qdt.common.infrastructure.mongo.converters;

import com.al.qdt.common.domain.base.DateTimeConverterBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testing ZonedDateTimeWriteConverter class")
class ZonedDateTimeWriteConverterTest extends DateTimeConverterBaseTest {
    final ZonedDateTimeWriteConverter zonedDateTimeWriteConverter = new ZonedDateTimeWriteConverter();

    @Test
    @DisplayName("Testing convert() method")
    void convertTest() {
        final var actualZonedDateTime = ZonedDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, NANOSECOND, UTC);
        final var actualDate = this.zonedDateTimeWriteConverter.convert(actualZonedDateTime);

        assertNotNull(actualDate);
        assertThat(actualDate).isInstanceOf(Date.class);
        assertEquals(MILLISECONDS_SINCE_JAN_1970, actualDate.getTime());
    }
}
