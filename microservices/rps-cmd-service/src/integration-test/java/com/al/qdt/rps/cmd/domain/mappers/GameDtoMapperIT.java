package com.al.qdt.rps.cmd.domain.mappers;

import com.al.qdt.common.api.dto.GameDto;
import com.al.qdt.rps.cmd.base.AbstractIntegrationTests;
import com.al.qdt.rps.cmd.base.DtoTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.al.qdt.common.enums.Hand.ROCK;
import static com.al.qdt.common.helpers.Constants.USERNAME_ONE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Integration testing of the GameDtoMapper interface")
@Tag(value = "mapper")
class GameDtoMapperIT extends AbstractIntegrationTests implements DtoTests {

    @Autowired
    GameDtoMapper gameDtoMapper;

    @Order(1)
    @Test
    @DisplayName("Testing injections")
    void injectionTest() {
        assertNotNull(this.gameDtoMapper);
    }

    @Test
    @DisplayName("Testing of the fromDto() method with valid parameters")
    void fromDtoTest() {
        final var gameDto = createGameDto(USERNAME_ONE);
        final var playGameCommand = this.gameDtoMapper.fromDto(gameDto);

        assertNotNull(playGameCommand);
        assertEquals(USERNAME_ONE, playGameCommand.getUsername());
        assertEquals(gameDto.getUsername(), playGameCommand.getUsername());
        assertEquals(ROCK, playGameCommand.getHand());
        assertEquals(gameDto.getHand(), playGameCommand.getHand().name());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing of the fromDto() method with null parameter")
    void fromDtoWithNullTest(GameDto gameDto) {
        final var playGameCommand = assertDoesNotThrow(() -> this.gameDtoMapper.fromDto(gameDto));

        assertNull(playGameCommand);
    }
}
