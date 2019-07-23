package org.needlehack.collector.usecases.rss.params;

import org.needlehack.collector.domain.exceptions.EntityValidationException;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.usecases.UseCaseParams;

import java.net.MalformedURLException;
import java.net.URL;

public class CollectingFeedParams implements UseCaseParams {

    private final Feed feed;

    public CollectingFeedParams(Feed feed) {
        this.feed = feed;
    }

    @Override
    public String toString() {
        return "CollectingFeedParams{" +
                "feed=" + feed +
                '}';
    }

    public Feed createObject() {
        return (Feed) feed.clone();
    }
}
