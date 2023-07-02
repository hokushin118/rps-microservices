package com.al.qdt.common.providers;

import com.al.qdt.common.domain.base.EventTests;
import com.al.qdt.common.domain.enums.Player;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Custom arguments provider class to create {@link Player} enum converter test arguments.
 */
public class PlayerEnumConverterArgsProvider implements ArgumentsProvider, EventTests {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(Player.DRAW.name(), Player.DRAW),
                Arguments.of(Player.USER.name(), Player.USER),
                Arguments.of(Player.MACHINE.name(), Player.MACHINE),
                Arguments.of(Player.DRAW.name().toLowerCase(), Player.DRAW),
                Arguments.of(Player.USER.name().toLowerCase(), Player.USER),
                Arguments.of(Player.MACHINE.name().toLowerCase(), Player.MACHINE));
    }
}
