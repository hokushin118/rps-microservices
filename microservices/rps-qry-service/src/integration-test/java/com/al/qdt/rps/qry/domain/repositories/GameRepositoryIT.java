package com.al.qdt.rps.qry.domain.repositories;

import com.al.qdt.cqrs.queries.SortingOrder;
import com.al.qdt.rps.qry.base.EntityTests;
import com.al.qdt.rps.qry.infrastructure.config.TestConfig;
import com.al.qdt.rps.qry.domain.entities.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static com.al.qdt.common.domain.enums.Hand.ROCK;
import static com.al.qdt.common.domain.enums.Hand.SCISSORS;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID;
import static com.al.qdt.common.infrastructure.helpers.Constants.TEST_UUID_TWO;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_ONE_ID;
import static com.al.qdt.common.infrastructure.helpers.Constants.USER_TWO_ID;
import static com.al.qdt.common.infrastructure.helpers.Utils.getSortingOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("it")
@DisplayName("Integration testing of the GameRepository interface")
@Tag(value = "repository")
class GameRepositoryIT implements EntityTests {

    Game game;
    Game expectedGame;

    @Autowired
    GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        this.game = createGame(TEST_UUID, USER_ONE_ID, ROCK);
        this.expectedGame = this.gameRepository.save(this.game);

        assertNotNull(this.expectedGame);
        assertEquals(TEST_UUID, this.expectedGame.getId());
        assertEquals(this.game.getId(), this.expectedGame.getId());
    }

    @AfterEach
    void tearDown() {
        this.gameRepository.deleteAll();
        this.expectedGame = null;
    }

    @Order(1)
    @Test
    @DisplayName("Testing injections")
    void injectionTest() {
        assertNotNull(this.gameRepository);
    }

    @Test
    @DisplayName("Testing database init count")
    void repositoryInitCountTest() {
        final var count = this.gameRepository.count();

        assertEquals(1, count);
    }

    @Test
    @DisplayName("Testing getById() method")
    void getByIdTest() {
        final var actualGame = this.gameRepository.getById(TEST_UUID);

        assertNotNull(actualGame);
        assertEquals(TEST_UUID, actualGame.getId());
        assertEquals(this.expectedGame.getId(), actualGame.getId());
    }

    @Test
    @DisplayName("Testing findByUserId() method")
    void findGameByUserIdTest() {
        final var actualGames = this.gameRepository.findByUserId(USER_ONE_ID, createPageRequest());

        assertNotNull(actualGames);
        assertThat(actualGames.getContent(), not(empty()));
        assertThat(actualGames.getContent(), hasSize(1));
        assertEquals(USER_ONE_ID, actualGames.getContent().get(0).getUserId());
        assertEquals(this.expectedGame.getUserId(), actualGames.getContent().get(0).getUserId());
    }

    @Test
    @DisplayName("Testing deleteById() method")
    void deleteByIdTest() {
        assertDoesNotThrow(() -> this.gameRepository.deleteById(TEST_UUID));

        final var count = this.gameRepository.count();

        assertEquals(0, count);
    }

    @Test
    @DisplayName("Testing existsById() method")
    void existsByIdTest() {
        final var isExists = this.gameRepository.existsById(TEST_UUID);

        assertTrue(isExists);
    }

    @Test
    @DisplayName("Testing save() method")
    void saveTest() {
        final var newGame = createGame(TEST_UUID_TWO, USER_TWO_ID, SCISSORS);
        assertDoesNotThrow(() -> this.gameRepository.save(newGame));

        final var count = this.gameRepository.count();

        assertEquals(2, count);

        final var savedGameOptional = this.gameRepository.findById(newGame.getId());

        assertNotNull(savedGameOptional);
        assertTrue(savedGameOptional.isPresent());

        final var savedGame = savedGameOptional.get();

        assertNotNull(savedGame);
        assertEquals(TEST_UUID_TWO, savedGame.getId());
        assertEquals(newGame.getId(), savedGame.getId());
        assertEquals(USER_TWO_ID, savedGame.getUserId());
        assertEquals(newGame.getUserId(), savedGame.getUserId());
        assertEquals(SCISSORS, savedGame.getHand());
        assertEquals(newGame.getHand(), savedGame.getHand());
    }

    @Test
    @DisplayName("Testing uniqueIdentity")
    void uniqueIdentityTest() {
        final var gameWithSameId = createGame(TEST_UUID, USER_ONE_ID, ROCK);

        assertThrows(DataIntegrityViolationException.class, () -> this.gameRepository.save(gameWithSameId));
    }

    /**
     * Creating test page request.
     *
     * @return page request
     */
    private static PageRequest createPageRequest() {
        // creating Sort instance
        final var sort = getSortingOrder("id", SortingOrder.ASC);
        // creating Pageable instance
        return PageRequest.of(1, 10, sort);
    }
}
