package org.needlehack.collector.infrastructure.batch.reader.dto;

import java.util.ArrayList;
import java.util.List;

public class FeedLastInfoCollection {

    private List<FeedLastInfo> feedLastInfoIt = new ArrayList<>();

    public FeedLastInfoCollection() {
    }

    public FeedLastInfoCollection(List<FeedLastInfo> feedLastInfoIt) {
        this.feedLastInfoIt = feedLastInfoIt;
    }

    public List<FeedLastInfo> getFeedLastInfoIt() {
        return feedLastInfoIt;
    }

    public void setFeedLastInfoIt(List<FeedLastInfo> feedLastInfoIt) {
        this.feedLastInfoIt = feedLastInfoIt;
    }
}
