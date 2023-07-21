package com.al.qdt.rps.qry.providers;

import com.al.qdt.rps.qry.base.ProtoTests;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_ADMIN_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.GAME_EXPECTED_JSON;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;

/**
 * Custom arguments provider class to proto arguments.
 */
public class ProtoArgsProvider implements ArgumentsProvider, ProtoTests {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(GAME_EXPECTED_JSON, createGameProtoDto(USER_ONE_ID)),
                Arguments.of(GAME_ADMIN_EXPECTED_JSON, createGameAdminProtoDto(USER_ONE_ID)));
    }
}
