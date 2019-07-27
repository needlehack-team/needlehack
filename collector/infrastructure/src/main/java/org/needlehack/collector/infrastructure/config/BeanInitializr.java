package org.needlehack.collector.infrastructure.config;

import org.needlehack.collector.domain.events.DomainEventPublisher;
import org.needlehack.collector.domain.model.feed.ExtractorService;
import org.needlehack.collector.domain.model.feed.FeedListener;
import org.needlehack.collector.domain.model.feed.FeedRepository;
import org.needlehack.collector.usecases.feed.AppendFeedItem;
import org.needlehack.collector.usecases.feed.CreateFeedItem;
import org.needlehack.collector.usecases.rss.CollectFeed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInitializr {

    @Bean
    public CollectFeed collectFeed(FeedListener feedListener, DomainEventPublisher domainEventPublisher) {
        return new CollectFeed(feedListener, domainEventPublisher);
    }

    @Bean
    public AppendFeedItem appendFeed(ExtractorService extractorService, DomainEventPublisher domainEventPublisher) {
        return new AppendFeedItem(extractorService, domainEventPublisher);
    }

    @Bean
    public CreateFeedItem createFeedItem(FeedRepository elasticFeedRepository, DomainEventPublisher domainEventPublisher) {
        return new CreateFeedItem(elasticFeedRepository, domainEventPublisher);
    }
}
