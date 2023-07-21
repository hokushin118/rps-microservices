package com.al.qdt.score.qry.providers

import com.al.qdt.score.qry.base.ProtoTests
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

import java.util.stream.Stream

import static com.al.qdt.common.infrastructure.helpers.Constants.SCORE_ADMIN_EXPECTED_JSON
import static com.al.qdt.common.infrastructure.helpers.Constants.SCORE_EXPECTED_JSON

/**
 * Custom arguments provider class to proto arguments.
 */
class ProtoArgsProvider implements ArgumentsProvider, ProtoTests {

    @Override
    Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                Arguments.of(SCORE_ADMIN_EXPECTED_JSON, createScoreAdminProtoDto()),
                Arguments.of(SCORE_EXPECTED_JSON, createScoreProtoDto()));
    }
}
