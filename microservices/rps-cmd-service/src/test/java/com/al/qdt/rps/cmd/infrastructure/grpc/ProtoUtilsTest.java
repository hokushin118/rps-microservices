package com.al.qdt.rps.cmd.infrastructure.grpc;

import com.al.qdt.common.infrastructure.grpc.helpers.ProtoUtils;
import com.al.qdt.rps.cmd.providers.ProtoArgsProvider;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testing ProtoUtils class")
class ProtoUtilsTest {

    @ParameterizedTest(name = "{arguments}")
    @ArgumentsSource(ProtoArgsProvider.class)
    @DisplayName("Testing conversion of proto3 message to json string")
    <T extends Message> void toJsonTest(String expectedJson, T proto) throws JSONException, InvalidProtocolBufferException {
        final var actualJson = ProtoUtils.toJson(proto);

        assertNotNull(actualJson);

        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    @ParameterizedTest(name = "{arguments}")
    @ArgumentsSource(ProtoArgsProvider.class)
    @DisplayName("Testing conversion of json string to proto3 message")
    <T extends Message> void toProtoTest(String expectedJson, T proto) throws InvalidProtocolBufferException {
        final var actualProto = ProtoUtils.toProto(expectedJson, proto.getDefaultInstanceForType());

        assertNotNull(actualProto);
        assertEquals(proto, actualProto);
    }
}
