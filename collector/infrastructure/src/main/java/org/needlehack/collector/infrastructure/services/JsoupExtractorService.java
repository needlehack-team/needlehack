package org.needlehack.collector.infrastructure.services;

import org.needlehack.collector.domain.model.feed.ExtractorService;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.Topic;
import org.needlehack.collector.infrastructure.crawl.CrawlerFactory;
import org.needlehack.collector.infrastructure.crawl.ICrawler;
import org.needlehack.collector.infrastructure.crawl.dto.CrawlSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class JsoupExtractorService implements ExtractorService {

    private HashMap<String, ICrawler> extractorPerOrigin = new HashMap<String, ICrawler>();

    @Autowired
    private CrawlerFactory crawlerFactory;

    @Override
    public FeedItem insertContentAndTopics(FeedItem feedItem) {

        FeedItem feedItemComplete = feedItem;

        if (StringUtils.isEmpty(feedItem.getContent()) || CollectionUtils.isEmpty(feedItem.getTopics())) {
            ICrawler extractor = getExtractor(feedItem.getOrigin());

            if (extractor != null) {
                CrawlSection sectionsToExtract = extractor.crawl(feedItem.getUri());
                feedItemComplete = feedItem.withContent(sectionsToExtract.getContent())
                        .withTopics(sectionsToExtract.getTags()
                                .stream()
                                .map(tag -> new Topic(tag))
                                .collect(Collectors.toSet()));
            }
        }
        return feedItemComplete;
    }


    private ICrawler getExtractor(Feed feed) {

        ICrawler cacheCrawler = extractorPerOrigin.get(feed.getSource());

        if (cacheCrawler == null) {
            cacheCrawler = crawlerFactory.getCrawler(feed.getSource());
            extractorPerOrigin.put(feed.getSource(), cacheCrawler);
        }

        return cacheCrawler;
    }
}
