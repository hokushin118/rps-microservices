package com.al.qdt.rps.cmd.domain.mappers;

import com.al.qdt.common.api.dto.GameDto;
import com.al.qdt.rps.cmd.base.DtoTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Testing of the GameDtoMapper interface")
@Tag(value = "mapper")
class GameDtoMapperTest implements DtoTests {
    final GameDtoMapper gameDtoMapper = Mappers.getMapper(GameDtoMapper.class);

    @Test
    @DisplayName("Testing of the fromDto() method with valid parameters")
    void fromDtoTest() {
        final var gameDto = createGameDto(USER_ONE_ID);
        final var playGameCommand = this.gameDtoMapper.fromDto(gameDto);

        assertNotNull(playGameCommand);
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
