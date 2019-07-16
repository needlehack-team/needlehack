package org.needlehack.collector.infrastructure.services;

import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RssFeedListenerTest {

    private final static Logger log = LoggerFactory.getLogger(RssFeedListenerTest.class);

    private final static String URI_ORIGIN = "Netflix";
    private static URL URI_FEED;

    static {
        try {
            URI_FEED = new URL("https://medium.com/feed/netflix-techblog");
        } catch (MalformedURLException exception) {
            log.error("URL from [{0}] feed has not been well-formed", URI_ORIGIN, exception);
        }
    }

    RssFeedListener feedListener = new RssFeedListener();

    @Test
    public void given_A_Feed_When_Content_Is_Extracted_Then_A_Collection_Of_Posts_Is_Returned() {
        // given
        Feed feed = new Feed(URI_FEED, URI_ORIGIN);

        // when
        final List<FeedItem> postsCollected = feedListener.extract(feed);

        // then
        assertTrue(postsCollected != null);
        assertTrue(postsCollected.size() > 0);
        assertTrue(postsCollected.stream()
                .filter(post -> !post.getOrigin()
                        .getSource()
                        .equals(URI_ORIGIN))
                .count() == 0);
    }
}
