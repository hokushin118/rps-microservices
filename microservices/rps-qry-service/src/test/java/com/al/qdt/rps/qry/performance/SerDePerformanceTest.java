package com.al.qdt.rps.qry.performance;

import com.al.qdt.rps.qry.api.dto.GameDto;
import com.al.qdt.rps.qry.base.DtoTests;
import com.al.qdt.rps.qry.base.ProtoTests;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;

import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DisplayName("Testing serialization / deserialization performance")
class SerDePerformanceTest implements DtoTests, ProtoTests {
    private static final long RUNS_NUMBER = 1_000_000L; // number of serde runs

    GameDto expectedGameJsonDto;
    com.al.qdt.rps.grpc.v1.dto.GameDto expectedGameProtoDto;

    @BeforeEach
    void setUp() {
        this.expectedGameJsonDto = createGameDto(USER_ONE_ID);
        this.expectedGameProtoDto = createGameProtoDto(USER_ONE_ID);
    }

    @AfterEach
    void tearDown() {
        this.expectedGameJsonDto = null;
        this.expectedGameProtoDto = null;
    }

    @Test
    @DisplayName("Testing GameDto serialization length")
    void serializationLengthTest() throws JsonProcessingException {
        final var jsonBytes = objectMapper.writeValueAsBytes(this.expectedGameJsonDto);
        final var jsonObjectLength = jsonBytes.length;
        log.info("GameDTO JSON: length : {} bytes", jsonObjectLength);
        final var protoBytes = this.expectedGameProtoDto.toByteArray();
        final var protoMessageLengthLength = protoBytes.length;
        log.info("GameDTO PROTO: length : {} bytes", protoMessageLengthLength);
        assertTrue(protoMessageLengthLength < jsonObjectLength);
    }

    @Test
    @DisplayName("Testing GameDto serialization / deserialization performance")
    void serializationDeserializationPerformanceTest() {
        final Runnable jsonSerDeTask = () -> {
            try {
                performObjectSerDe(expectedGameJsonDto, GameDto.class);
            } catch (Exception e) {
                log.error("Error occurred while performing json serialization / deserialization: ", e);
            }
        };
        final Runnable protoSerDeTask = () -> {
            try {
                performMessageSerDe(expectedGameProtoDto, com.al.qdt.rps.grpc.v1.dto.GameDto.parser());
            } catch (Exception e) {
                log.error("Error occurred while performing proto 3 message serialization / deserialization: ", e);
            }
        };
        final var jsonSerDeTime = runTask(jsonSerDeTask, RUNS_NUMBER);
        log.info("GameDTO JSON: Elapsed time for {} serde runs : {} ms", RUNS_NUMBER, jsonSerDeTime);
        final var protoSerDeTime = runTask(protoSerDeTask, RUNS_NUMBER);
        log.info("GameDTO PROTO: Elapsed time for {} serde runs : {} ms", RUNS_NUMBER, protoSerDeTime);
        assertTrue(protoSerDeTime < jsonSerDeTime);
    }

    /**
     * Utility method for running tasks.
     *
     * @param task  task
     * @param count number of runs
     * @return elapsed time
     */
    private static long runTask(Runnable task, long count) {
        final var start = System.currentTimeMillis();
        for (var i = 0; i < count; i++) {
            task.run();
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Serializes and deserialize pojo.
     *
     * @param obj   object
     * @param clazz class type
     * @param <T>   object type
     */
    private static <T extends Serializable> void performObjectSerDe(T obj, Class<T> clazz) throws IOException {
        // serialize object
        final var bytes = objectMapper.writeValueAsBytes(obj);
        // deserialize object
        objectMapper.readValue(bytes, clazz);
    }

    /**
     * Serializes and deserializes proto 3 message.
     *
     * @param message proto3 message
     * @param parser  proto3 message parser
     * @param <T>     proto 3 message type
     */
    private static <T extends Message> void performMessageSerDe(T message, Parser<T> parser) throws InvalidProtocolBufferException {
        // serialize proto 3 message
        final var bytes = message.toByteArray();
        // deserialize proto 3 message
        parser.parseFrom(bytes);
    }
}
