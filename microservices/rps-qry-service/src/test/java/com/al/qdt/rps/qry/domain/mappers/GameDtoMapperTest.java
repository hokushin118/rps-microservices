package com.al.qdt.rps.qry.domain.mappers;

import com.al.qdt.rps.qry.base.EntityTests;
import com.al.qdt.rps.qry.domain.entities.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Testing of the GameDtoMapper interface")
@Tag(value = "mapper")
class GameDtoMapperTest implements EntityTests {
    final GameDtoMapper gameDtoMapper = Mappers.getMapper(GameDtoMapper.class);

    @Test
    @DisplayName("Testing of the toGameDto() method")
    void toGameDtoTest() {
        final var game = createGame(TEST_UUID, USER_ONE_ID, ROCK);
        final var gameDto = this.gameDtoMapper.toGameDto(game);

        assertNotNull(gameDto);
        assertEquals(TEST_ID, gameDto.getId());
        assertEquals(game.getId().toString(), gameDto.getId());
        assertEquals(ROCK.name(), gameDto.getHand());
        assertEquals(game.getHand().name(), gameDto.getHand());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing of the toGameDto() method with null parameter")
    void toGameDtoWithNullTest(Game game) {
        final var gameDto = assertDoesNotThrow(() -> this.gameDtoMapper.toGameDto(game));

        assertNull(gameDto);
    }

    @Test
    @DisplayName("Testing of the toGameAdminDto() method")
    void toGameAdminDtoTest() {
        final var game = createGame(TEST_UUID, USER_ONE_ID, ROCK);
        final var gameAdminDto = this.gameDtoMapper.toGameAdminDto(game);

        assertNotNull(gameAdminDto);
        assertEquals(TEST_ID, gameAdminDto.getId());
        assertEquals(game.getId().toString(), gameAdminDto.getId());
        assertEquals(USER_ONE_ID.toString(), gameAdminDto.getUserId());
        assertEquals(game.getUserId().toString(), gameAdminDto.getUserId());
        assertEquals(ROCK.name(), gameAdminDto.getHand());
        assertEquals(game.getHand().name(), gameAdminDto.getHand());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing of the toGameAdminDto() method with null parameter")
    void toGameAdminDtoWithNullTest(Game game) {
        final var gameAdminDto = assertDoesNotThrow(() -> this.gameDtoMapper.toGameAdminDto(game));

        assertNull(gameAdminDto);
    }
}
