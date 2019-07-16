package org.needlehack.collector.domain.model.feed;

import java.util.List;

public interface FeedListener {

    List<FeedItem> extract(Feed feed);
}
