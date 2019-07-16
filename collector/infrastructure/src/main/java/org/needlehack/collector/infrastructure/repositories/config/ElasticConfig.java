package org.needlehack.collector.infrastructure.repositories.config;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {

        Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")};

        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(elasticsearchHost, 9200, "http"));
        restClientBuilder.setMaxRetryTimeoutMillis(3000); //Currently, set as default
        restClientBuilder.setDefaultHeaders(headers);


        RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);
        return client;
    }
}
