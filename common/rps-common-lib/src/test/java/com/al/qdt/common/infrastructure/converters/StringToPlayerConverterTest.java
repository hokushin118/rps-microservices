package com.al.qdt.common.infrastructure.converters;

import com.al.qdt.common.domain.enums.Player;
import com.al.qdt.common.providers.PlayerEnumConverterArgsProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.core.convert.converter.Converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testing StringToPlayerConverter class")
class StringToPlayerConverterTest {

    Converter<String, Player> stringToPlayerConverter;

    @BeforeEach
    void setUp() {
        this.stringToPlayerConverter = new StringToPlayerConverter();
    }

    @AfterEach
    void tearDown() {
        this.stringToPlayerConverter = null;
    }

    @ParameterizedTest(name = "{arguments}")
    @ArgumentsSource(PlayerEnumConverterArgsProvider.class)
    @DisplayName("Testing enum convert method")
    void convert(String argument, Player expected) {
        final var actual =this.stringToPlayerConverter.convert(argument);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
