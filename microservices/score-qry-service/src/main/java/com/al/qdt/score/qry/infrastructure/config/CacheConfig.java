package com.al.qdt.score.qry.infrastructure.config;

import com.al.qdt.common.infrastructure.properties.RpsCacheProperties;
import com.al.qdt.rps.grpc.v1.dto.ScoreAdminDto;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresAdminResponse;
import com.al.qdt.rps.grpc.v1.services.ListOfScoresResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {
    public static final String SCORE_CACHE_NAMES = "scoresCache";
    public static final String SCORES_ADMIN_CACHE_NAME = "scoresAdmin";
    public static final String SCORES_ADMIN_PROTO_CACHE_NAME = "scoresAdminProto";
    public static final String SCORE_ADMIN_CACHE_NAME = "scoreAdmin";
    public static final String SCORE_ADMIN_PROTO_CACHE_NAME = "scoreAdminProto";
    public static final String SCORES_ADMIN_USER_ID_CACHE_NAME = "scoresAdminUserId";
    public static final String SCORES_ADMIN_USER_ID_PROTO_CACHE_NAME = "scoresAdminUserIdProto";
    public static final String SCORES_ADMIN_USER_ID_WINNER_CACHE_NAME = "userIdWinnerScores";
    public static final String SCORES_ADMIN_USER_ID_WINNER_PROTO_CACHE_NAME = "userIdWinnerScoresProto";
    public static final String WINNERS_ADMIN_CACHE_NAME = "scoresAdminWinners";
    public static final String WINNERS_ADMIN_PROTO_CACHE_NAME = "scoresAdminWinnersProto";
    public static final String SCORES_MY_CACHE_NAME = "myScores";
    public static final String SCORES_MY_PROTO_CACHE_NAME = "myScoresProto";
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
        cacheManager.setCaches(List.of(new ConcurrentMapCache(SCORE_ADMIN_CACHE_NAME),
                new ConcurrentMapCache(SCORES_ADMIN_CACHE_NAME),
                new ConcurrentMapCache(SCORE_ADMIN_PROTO_CACHE_NAME),
                new ConcurrentMapCache(SCORES_ADMIN_PROTO_CACHE_NAME),
                new ConcurrentMapCache(SCORES_ADMIN_USER_ID_CACHE_NAME),
                new ConcurrentMapCache(SCORES_ADMIN_USER_ID_PROTO_CACHE_NAME),
                new ConcurrentMapCache(SCORES_MY_CACHE_NAME),
                new ConcurrentMapCache(SCORES_MY_PROTO_CACHE_NAME),
                new ConcurrentMapCache(WINNERS_ADMIN_CACHE_NAME),
                new ConcurrentMapCache(WINNERS_ADMIN_PROTO_CACHE_NAME)));

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
                .withCacheConfiguration(SCORE_ADMIN_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(SCORE_ADMIN_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ScoreAdminDto.class, CACHE_DURATION_MIN))
                .withCacheConfiguration(SCORES_ADMIN_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(SCORES_ADMIN_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ListOfScoresAdminResponse.class, CACHE_DURATION_MIN))
                .withCacheConfiguration(WINNERS_ADMIN_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(WINNERS_ADMIN_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ListOfScoresAdminResponse.class, CACHE_DURATION_MIN))
                .withCacheConfiguration(SCORES_ADMIN_USER_ID_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(SCORES_ADMIN_USER_ID_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ListOfScoresAdminResponse.class, CACHE_DURATION_MIN))
                .withCacheConfiguration(SCORES_MY_CACHE_NAME, redisCacheConfiguration(CACHE_DURATION_MIN))
                .withCacheConfiguration(SCORES_MY_PROTO_CACHE_NAME, redisProtoCacheConfiguration(ListOfScoresResponse.class, CACHE_DURATION_MIN));
    }
}
