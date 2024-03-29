package com.al.qdt.rps.qry.db;

import com.al.qdt.rps.qry.base.AbstractIntegrationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Integration testing of the datasource")
@Tag(value = "database")
class DatabaseConnectionPoolIT extends AbstractIntegrationTests {
    static final String DATASOURCE_NAME = "com.zaxxer.hikari.HikariDataSource";

    @Autowired
    DataSource dataSource;

    @Order(1)
    @Test
    @DisplayName("Testing injections")
    void injectionTest() {
        assertNotNull(this.dataSource);
    }

    @Test
    @DisplayName("Testing datasource type")
    void dataSourceTest() {
        assertEquals(DATASOURCE_NAME, dataSource.getClass().getName());
    }
}
