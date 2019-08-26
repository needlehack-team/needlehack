package com.needlehack.searchapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.elasticsearch.client.RestHighLevelClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
class SearchApiConfig(@Value("\${elasticsearch.url.schema}") val urlConnectionSchema: String,
                      @Value("\${elasticsearch.url.host}") val urlConnectionHost: String,
                      @Value("\${elasticsearch.url.port}") val urlConnectionPort: Int) {

    val log = LoggerFactory.getLogger(SearchApiConfig::class.java)

    @Bean(destroyMethod = "close")
    fun highLevelClient(restClient: RestClientBuilder): RestHighLevelClient {
        return RestHighLevelClient(restClient)
    }

    @Bean
    fun elasticRestClient(): RestClientBuilder {
        log.info("Connecting to elasticsearch located in the address: $urlConnectionSchema://$urlConnectionHost:$urlConnectionPort")
        return RestClient.builder(HttpHost(urlConnectionHost, urlConnectionPort, urlConnectionSchema))
    }

    @Bean
    @Primary
    fun jacksonObjectMapper() = ObjectMapper().registerModule(KotlinModule())

}