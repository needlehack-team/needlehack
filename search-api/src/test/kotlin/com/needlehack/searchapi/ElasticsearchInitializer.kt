package com.needlehack.searchapi

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class ElasticsearchInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    val ELASTICSEARCH_DEFAULT_PORT = 9200;

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
                "elasticsearch.url.host=" + SearchApiApplicationTests.elasticsearchContainer.containerIpAddress,
                "elasticsearch.url.port=" + SearchApiApplicationTests.elasticsearchContainer.getMappedPort(ELASTICSEARCH_DEFAULT_PORT)
        ).applyTo(configurableApplicationContext.environment)
    }
}

