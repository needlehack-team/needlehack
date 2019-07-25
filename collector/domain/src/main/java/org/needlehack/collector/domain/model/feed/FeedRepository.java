package org.needlehack.collector.domain.model.feed;

/**
 * Repository
 */
public interface FeedRepository {

    FeedItem create(FeedItem feedItem);

    FeedItem findOne(String itemId);

}
