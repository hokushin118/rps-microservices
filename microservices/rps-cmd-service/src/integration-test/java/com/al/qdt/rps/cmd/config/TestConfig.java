package com.al.qdt.rps.cmd.config;

import com.al.qdt.common.infrastructure.commands.CommandDispatcherImpl;
import com.al.qdt.common.infrastructure.commands.EventSourcingHandlerImpl;
import com.al.qdt.common.mongo.repositories.EventRepository;
import com.al.qdt.cqrs.handlers.EventSourcingHandler;
import com.al.qdt.cqrs.infrastructure.CommandDispatcher;
import com.al.qdt.cqrs.infrastructure.EventStore;
import com.al.qdt.cqrs.producers.EventProducer;
import com.al.qdt.rps.cmd.aggregates.RpsAggregate;
import com.al.qdt.rps.cmd.handlers.CommandHandler;
import com.al.qdt.rps.cmd.handlers.RpsCommandHandler;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * Test configuration for integration tests with partial context loading.
 * Modification of the Spring context during the runtime.
 */
@TestConfiguration
public class TestConfig {

    @MockBean
    MongoProperties mongoProperties;

    @MockBean
    MongoTemplate mongoTemplate;

    @MockBean
    MongoDatabaseFactory mongoDatabaseFactory;

    @MockBean
    MappingMongoConverter mappingMongoConverter;

    @MockBean
    EventStore eventStore;

    @MockBean
    EventRepository eventRepository;

    @MockBean
    EventProducer eventProducer;

    @Bean
    EventSourcingHandler<RpsAggregate> eventSourcingHandler() {
        return new EventSourcingHandlerImpl<>(eventStore, eventProducer);
    }

    @Bean
    CommandDispatcher commandDispatcher() {
        return new CommandDispatcherImpl();
    }

    @Bean
    CommandHandler commandHandler() {
        return new RpsCommandHandler(eventSourcingHandler());
    }
}
