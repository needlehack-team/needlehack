package org.needlehack.collector.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.FeedListener;
import org.needlehack.collector.domain.model.feed.FeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticFeedItemRepositoryTestIT {

    private final static Logger log = LoggerFactory.getLogger(ElasticFeedItemRepositoryTestIT.class);

    private final static String URI_ORIGIN = "Netflix";
    private static URL URI_FEED;

    static {
        try {
            URI_FEED = new URL("https://medium.com/feed/netflix-techblog");
        } catch (MalformedURLException exception) {
            log.error("URL from [{0}] feed has not been well-formed", URI_ORIGIN, exception);
        }
    }

    @Autowired
    FeedListener rssFeedListener;

    @Autowired
    FeedRepository elasticFeedItemRepository;

    @Test
    public void given_A_Feed_Which_Has_Been_Consumed_Previously_When_Content_Is_Stored_Then_Items_Are_Not_Duplicated() {
        // given
        Feed feed = new Feed(URI_FEED, URI_ORIGIN);
        final List<FeedItem> postsCollected = rssFeedListener.extract(feed);
        postsCollected.forEach(post -> elasticFeedItemRepository.create(post));

        // when
        FeedItem feedItemDuplicated = postsCollected.get(0);
        elasticFeedItemRepository.create(feedItemDuplicated);

        // then
        assertNotNull(elasticFeedItemRepository.findOne(feedItemDuplicated.getGeneratedId()));
    }
}
