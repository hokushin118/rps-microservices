package com.al.qdt.rps.qry.infrastructure.config;

import com.al.qdt.common.infrastructure.properties.RpsCacheProperties;
import com.al.qdt.rps.grpc.v1.dto.GameAdminDto;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfGamesResponse;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.util.List;

import static com.al.qdt.common.infrastructure.redis.RedisUtils.redisCacheConfiguration;
import static com.al.qdt.common.infrastructure.redis.RedisUtils.redisProtoCacheConfiguration;

/**
 * Caching configuration.
 */
@Configuration
@EnableCaching
public class CacheConfig {
    public static final String GAME_CACHE_NAMES = "gamesCache";
    public static final String GAME_ADMIN_CACHE_NAME = "gameAdmin";
    public static final String GAME_ADMIN_PROTO_CACHE_NAME = "gameAdminProto";
    public static final String GAMES_ADMIN_CACHE_NAME = "gamesAdmin";
    public static final String GAMES_ADMIN_PROTO_CACHE_NAME = "gamesAdminProto";
    public static final String GAMES_ADMIN_USER_ID_CACHE_NAME = "gamesAdminUserId";
    public static final String GAMES_ADMIN_USER_ID_PROTO_CACHE_NAME = "gamesAdminUserIdProto";
    public static final String GAMES_MY_CACHE_NAME = "myGames";
    public static final String GAMES_MY_PROTO_CACHE_NAME = "myGamesProto";
    private static final long CACHE_DURATION_MIN = 60L; // cache time to live (TTL)

    /**
     * Specific cache settings of the application.
     *
     * @return cache settings
     */
    @Bean
    public RpsCacheProperties rpsCacheProperties() {
        return new RpsCacheProperties();
    }

    /**
     * Creates a {@link SimpleCacheManager} bean with the specified caches.
     *
     * @return simple cache manager bean
     */
    @Bean
    @Profile("it")
    public CacheManager cacheManager() {
        final var cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache(GAME_ADMIN_CACHE_NAME),
                new ConcurrentMapCache(GAMES_ADMIN_CACHE_NAME),
                new ConcurrentMapCache(GAME_ADMIN_PROTO_CACHE_NAME),
                new ConcurrentMapCache(GAMES_ADMIN_PROTO_CACHE_NAME),
                new ConcurrentMapCache(GAMES_MY_CACHE_NAME),
                new ConcurrentMapCache(GAMES_MY_PROTO_CACHE_NAME),
                new ConcurrentMapCache(GAMES_ADMIN_USER_ID_CACHE_NAME),
                new ConcurrentMapCache(GAMES_ADMIN_USER_ID_PROTO_CACHE_NAME)));

        // manually call initialize the caches as our SimpleCacheManager is not declared as a bean
        cacheManager.initializeCaches();

        return new TransactionAwareCacheManagerProxy(cacheManager);
    }

    /**
     * Creates a custom {@link RedisCacheConfiguration} Redis cache configuration.
     *
     * @return custom Redis cache configuration
     */
    @Bean
    @Profile("!it")
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(GAME_ADMIN_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(GAME_ADMIN_PROTO_CACHE_NAME, redisProtoCacheConfiguration(GameAdminDto.class, CACHE_DURATION_MIN))
                .withCacheConfiguration(GAMES_ADMIN_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(GAMES_ADMIN_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ListOfGamesAdminResponse.class, CACHE_DURATION_MIN))
                .withCacheConfiguration(GAMES_MY_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(GAMES_MY_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ListOfGamesResponse.class, CACHE_DURATION_MIN))
                .withCacheConfiguration(GAMES_ADMIN_USER_ID_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(GAMES_ADMIN_USER_ID_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ListOfGamesAdminResponse.class, CACHE_DURATION_MIN));
    }
}
