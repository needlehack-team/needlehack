package org.needlehack.collector.infrastructure.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
public class ElasticFeedItemRepository implements FeedRepository {

    private RestHighLevelClient client;

    private ObjectMapper objectMapper;

    @Autowired
    public ElasticFeedItemRepository(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public FeedItem create(FeedItem feedItem) {

        Map<String, Object> postMapper = objectMapper.convertValue(feedItem, Map.class);
        IndexRequest indexRequest = new IndexRequest("feed-collector").type("feedItem")
                .id(feedItem.getId())
                .source(postMapper);
        IndexResponse indexResponse = null;

        try {
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return feedItem;
    }
}
