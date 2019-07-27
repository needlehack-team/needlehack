package org.needlehack.collector.usecases.feed.params;

import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.usecases.UseCaseParams;

import java.util.Objects;

public class AppendingFeedItemParams implements UseCaseParams {

    private final FeedItem feedItem;

    public AppendingFeedItemParams(FeedItem feedItem) {
        this.feedItem = feedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppendingFeedItemParams that = (AppendingFeedItemParams) o;
        return Objects.equals(feedItem, that.feedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedItem);
    }

    @Override
    public String toString() {
        return "CreatingFeedItemParams{" + "feedItem=" + feedItem + '}';
    }

    public FeedItem createObject() {
        return (FeedItem) feedItem.clone();
    }
}
