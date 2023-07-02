package com.al.qdt.rps.qry.domain.mappers;

import com.al.qdt.rps.qry.base.AbstractIntegrationTests;
import com.al.qdt.rps.qry.base.EntityTests;
import com.al.qdt.rps.qry.domain.entities.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Integration testing of the GameProtoMapper interface")
@Tag(value = "mapper")
class GameProtoMapperIT extends AbstractIntegrationTests implements EntityTests {

    @Autowired
    GameProtoMapper gameProtoMapper;

    @Order(1)
    @Test
    @DisplayName("Testing injections")
    void injectionTest() {
        assertNotNull(this.gameProtoMapper);
    }

    @Test
    @DisplayName("Testing of the toGameDto() method")
    void toGameDtoTest() {
        final var game = createGame(TEST_UUID, USER_ONE_ID, ROCK);
        final var gameDto = this.gameProtoMapper.toGameDto(game);

        assertNotNull(gameDto);
        assertEquals(TEST_ID, gameDto.getId());
        assertEquals(game.getId().toString(), gameDto.getId());
        assertEquals(ROCK.name(), gameDto.getHand().name());
        assertEquals(game.getHand().name(), gameDto.getHand().name());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing of the toGameDto() method with null parameter")
    void toGameDtoWithNullTest(Game game) {
        final var gameDto = assertDoesNotThrow(() -> this.gameProtoMapper.toGameDto(game));

        assertNull(gameDto);
    }

    @Test
    @DisplayName("Testing of the toGameAdminDto() method")
    void toGameAdminDtoTest() {
        final var game = createGame(TEST_UUID, USER_ONE_ID, ROCK);
        final var gameAdminDto = this.gameProtoMapper.toGameAdminDto(game);

        assertNotNull(gameAdminDto);
        assertEquals(TEST_ID, gameAdminDto.getId());
        assertEquals(game.getId().toString(), gameAdminDto.getId());
        assertEquals(USER_ONE_ID.toString(), gameAdminDto.getUserId());
        assertEquals(game.getUserId().toString(), gameAdminDto.getUserId());
        assertEquals(ROCK.name(), gameAdminDto.getHand().name());
        assertEquals(game.getHand().name(), gameAdminDto.getHand().name());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Testing of the toGameAdminDto() method with null parameter")
    void toGameAdminDtoWithNullTest(Game game) {
        final var gameAdminDto = assertDoesNotThrow(() -> this.gameProtoMapper.toGameAdminDto(game));

        assertNull(gameAdminDto);
    }
}
