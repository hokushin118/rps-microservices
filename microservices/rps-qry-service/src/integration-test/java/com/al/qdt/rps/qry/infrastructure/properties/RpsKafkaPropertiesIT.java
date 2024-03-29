package com.al.qdt.rps.qry.infrastructure.properties;

import com.al.qdt.common.infrastructure.properties.RpsKafkaProperties;
import com.al.qdt.rps.qry.base.AbstractIntegrationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Integration testing of the RpsKafkaProperties class")
@Tag(value = "common")
class RpsKafkaPropertiesIT extends AbstractIntegrationTests {
    private static final String DLT_TOPIC_NAME = "DLT.RPS.QRY";
    private static final String CONSUMER_GROUP_ID = "rps-consumer-group";
    private static final Integer DLT_PARTITION_COUNT = 1;
    private static final Integer DLT_PARTITION_NUMBER = 0;
    private static final Long RETRIES_NUMBER = 2L;
    private static final Long RETRIES_DELAY_INTERVAL = 1L;

    @Autowired
    RpsKafkaProperties rpsKafkaProperties;

    @Order(1)
    @Test
    @DisplayName("Testing injections")
    void injectionTest() {
        assertNotNull(this.rpsKafkaProperties);
    }

    @Test
    @DisplayName("Testing injected properties")
    void propertiesTest() {
        assertEquals(DLT_TOPIC_NAME, this.rpsKafkaProperties.getDlqTopicName());
        assertEquals(CONSUMER_GROUP_ID, this.rpsKafkaProperties.getConsumerGroupId());
        assertEquals(DLT_PARTITION_COUNT, this.rpsKafkaProperties.getDlqPartitionCount());
        assertEquals(DLT_PARTITION_NUMBER, this.rpsKafkaProperties.getDlqPartitionNumber());
        assertEquals(RETRIES_NUMBER, this.rpsKafkaProperties.getRetriesNumber());
        assertEquals(RETRIES_DELAY_INTERVAL, this.rpsKafkaProperties.getRetriesDelayInterval());
    }
}
