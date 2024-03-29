package com.al.qdt.score.qry.infrastructure.propertirs

import com.al.qdt.common.infrastructure.properties.RpsExecutorProperties
import com.al.qdt.score.qry.base.AbstractIntegrationTests
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject
import spock.lang.Title

@Title("Integration testing of the RpsExecutorProperties class")
class RpsExecutorPropertiesITSpec extends AbstractIntegrationTests {
    static final EXECUTOR_CORE_POOL_SIZE = 2
    static final EXECUTOR_MAX_POOL_SIZE = 2
    static final EXECUTOR_QUEUE_CAPACITY = 500
    static final EXECUTOR_THREAD_NAME_PREFIX = "async-thread"

    @Subject
    @Autowired
    RpsExecutorProperties rpsExecutorProperties

    def 'Testing property injections'() {
        expect:
        assert rpsExecutorProperties
    }

    def 'Testing injected properties'() {
        expect:
        EXECUTOR_CORE_POOL_SIZE == rpsExecutorProperties.corePoolSize
        EXECUTOR_MAX_POOL_SIZE == rpsExecutorProperties.maxPoolSize
        EXECUTOR_QUEUE_CAPACITY == rpsExecutorProperties.queueCapacity
        EXECUTOR_THREAD_NAME_PREFIX == rpsExecutorProperties.threadNamePrefix
    }
}
