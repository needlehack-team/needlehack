package org.needlehack.collector.steps;

import com.google.common.io.Resources;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.FeedRepository;
import org.needlehack.collector.helpers.World;
import org.needlehack.collector.usecases.UseCase;
import org.needlehack.collector.usecases.rss.params.CollectingFeedParams;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class CollectFeedSteps extends AbstractStepsConfiguration {

    @Autowired
    private FeedRepository elasticFeedItemRepository;

    @Autowired
    private UseCase<CollectingFeedParams> collectFeed;

    @Autowired
    private World world;

    @Given("^an URI related with an external RSS feed$")
    public void uRIRelatedWithAnExternalRSSFeed() throws Throwable {
        world.setFeed(new MockFeed("rss/netflix-techblog.xml", "Netflix"));
    }

    @Given("^an URI related with an external RSS feed without content$")
    public void uRIRelatedWithAnExternalRSSFeedWithoutContent() throws Throwable {
        world.setFeed(new MockFeed("rss/reactnative-techblog.xml", "React Native"));
    }

    @When("^the collecting feed process is fired up$")
    public void collectingFeedProcessIsFiredUp() {
        List<FeedItem> feedItems = (List<FeedItem>) collectFeed.execute(new CollectingFeedParams(world.getFeed()
                .getSource(), world.getFeed()
                .getUri()
                .toString()));
        world.setFeedItemsCollected(feedItems);
    }

    @Then("^the feeds are collected$")
    public void feedsAreCollected() {
        assertNotNull(world.getFeedItemsCollected());
        assertFalse(world.getFeedItemsCollected()
                .isEmpty());
    }

    @Then("^the items have content and category related with them$")
    public void itemsHaveContentAndCategoryRelated() {
        //TODO
    }

    @Then("^all the feed items are stored in a data repository$")
    public void feedItemsAreStoredInADataRepository() {
        //TODO
    }


    class MockFeed extends Feed {
        public MockFeed(String fileName, String source) {
            super(Resources.getResource(fileName), source);
        }
    }
}
