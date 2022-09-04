package com.al.qdt.rps.cmd.domain.mappers;

import com.al.qdt.rps.cmd.base.AbstractIntegrationTests;
import com.al.qdt.rps.cmd.base.ProtoTests;
import com.al.qdt.rps.grpc.v1.dto.GameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.al.qdt.common.helpers.Constants.USERNAME_ONE;
import static com.al.qdt.rps.grpc.v1.common.Hand.ROCK;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Integration testing of the GameProtoMapper interface")
@Tag(value = "mapper")
class GameProtoMapperIT extends AbstractIntegrationTests implements ProtoTests {

    @Autowired
    GameProtoMapper gameProtoMapper;

    @Order(1)
    @Test
    @DisplayName("Testing injections")
    void injectionTest() {
        assertNotNull(this.gameProtoMapper);
    }

    @Test
    @DisplayName("Testing of the fromDto() method")
    void fromDtoTest() {
        final var gameDto = createGameDto();
        final var playGameCommand = this.gameProtoMapper.fromDto(gameDto);

        assertNotNull(playGameCommand);
        assertEquals(USERNAME_ONE, playGameCommand.getUsername());
        assertEquals(gameDto.getUsername(), playGameCommand.getUsername());
        assertEquals(ROCK.getValueDescriptor().getName(), playGameCommand.getHand().name());
        assertEquals(gameDto.getHand().getValueDescriptor().getName(), playGameCommand.getHand().name());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing of the fromDto() method with null parameter")
    void fromDtoWithNullTest(GameDto gameDto) {
        final var playGameCommand = assertDoesNotThrow(() -> this.gameProtoMapper.fromDto(gameDto));

        assertNull(playGameCommand);
    }
}
