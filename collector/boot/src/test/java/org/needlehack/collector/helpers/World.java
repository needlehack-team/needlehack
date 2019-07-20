package org.needlehack.collector.helpers;

import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class World {

    private Feed feed;

    private List<FeedItem> feedItemsCollected;

    public void reset() {
        feed = null;
        feedItemsCollected = new ArrayList<>();
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public void setFeedItemsCollected(List<FeedItem> feedItemsCollected) {
        this.feedItemsCollected = feedItemsCollected;
    }

    public List<FeedItem> getFeedItemsCollected() {
        return feedItemsCollected;
    }
}
