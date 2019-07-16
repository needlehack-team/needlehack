package org.needlehack.collector.steps;

import com.google.common.io.Resources;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.FeedListener;
import org.needlehack.collector.domain.model.feed.FeedRepository;
import org.needlehack.collector.helpers.World;
import org.needlehack.collector.infrastructure.repositories.ElasticFeedItemRepository;
import org.needlehack.collector.usecases.UseCase;
import org.needlehack.collector.usecases.rss.params.CollectingFeedParams;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CollectFeedSteps extends AbstractStepsConfiguration {

    @Autowired
    private UseCase<CollectingFeedParams> collectFeed;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ElasticFeedItemRepository elasticRepository;

    @Autowired
    private FeedListener feedListener;

    @Autowired
    private World world;

    @Given("^an URI related with an external RSS feed$")
    public void anURIRelatedWithAnExternalRSSFeed() throws Throwable {
        world.setFeed(new MockFeed("xml/netflix-techblog.xml", "Netflix"));
    }

    @When("^the collecting process is fired up$")
    public void theCollectingProcessIsFfiredUp() {
        List<FeedItem> postsCollected = (List<FeedItem>) collectFeed.execute(new CollectingFeedParams(world.getFeed()
                .getSource(), world.getFeed()
                .getUri()
                .toString()));
        world.setPostsCollected(postsCollected);
    }

    @Then("^the created posts are stored$")
    public void theCreatedPostsAreStored() {
        // TODO
    }

    class MockFeed extends Feed {
        public MockFeed(String fileName, String source) {
            super(Resources.getResource(fileName), source);
        }
    }
}
