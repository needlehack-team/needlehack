package org.needlehack.collector.infrastructure.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.FeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class ElasticFeedItemRepository implements FeedRepository {

    private static final Logger logger = LoggerFactory.getLogger(ElasticFeedItemRepository.class);


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
                .id(feedItem.getGeneratedId())
                .source(postMapper);

        client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                logger.debug("New feedItem indexed [{}]", indexResponse);
            }

            @Override
            public void onFailure(Exception e) {
                throw new RuntimeException(e);
            }
        });

        return feedItem;
    }

    @Override
    public FeedItem findOne(String itemId) {

        FeedItem feedItem;
        GetRequest getRequest = new GetRequest("feed-collector").type("feedItem")
                .id(itemId);

        try {
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            feedItem = objectMapper.convertValue(response.getSourceAsMap(), FeedItem.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return feedItem;
    }
}
