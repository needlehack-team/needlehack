package org.needlehack.collector.usecases.feed;

import org.needlehack.collector.domain.events.DomainEvent;
import org.needlehack.collector.domain.events.DomainEventPublisher;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedListener;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.usecases.rss.CollectFeed;
import org.needlehack.collector.usecases.rss.params.CollectingFeedParams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateFeedItemTest {


    private final FeedListener feedListener = mock(FeedListener.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);
    CollectFeed collectFeed;

    @Before
    public void setUp() throws Exception {
        collectFeed = new CollectFeed(feedListener, domainEventPublisher);
    }

    @Test
    public void given_A_Feed_When_Content_Is_Extracted_Then_A_Collection_Of_Posts_Is_Returned() throws Exception {
        // given
        Feed feed = new Feed(new URL("https://medium.com/feed/netflix-techblog"), "Netflix");

        FeedItem feedItemForTest = new FeedItem.FeedItemBuilder().withTitle("title")
                .withUri("uri")
                .withOrigin(feed)
                .withContent("content")
                .withCollectAt(Date.from(Instant.now()))
                .createFeedItem();
        List<FeedItem> postsCollectedForTest = Collections.singletonList(feedItemForTest);

        given(feedListener.extract(feed)).willReturn(postsCollectedForTest);

        // when
        final List<FeedItem> postsCollected = collectFeed.execute(new CollectingFeedParams(feed));

        // then
        assertTrue(postsCollected != null);
        assertTrue(postsCollected.size() > 0);
        assertTrue(postsCollected.stream()
                .filter(post -> !post.getOrigin()
                        .getSource()
                        .equals("Netflix"))
                .count() == 0);

        verify(domainEventPublisher, times(postsCollected.size()))
                .publish(any(DomainEvent.class));
    }
}
