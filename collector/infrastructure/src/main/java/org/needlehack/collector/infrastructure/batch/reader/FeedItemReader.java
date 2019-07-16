package org.needlehack.collector.infrastructure.batch.reader;

import com.google.common.collect.Maps;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.needlehack.collector.domain.model.feed.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Component
public class FeedItemReader extends ItemReaderAdapter<Entry<Feed, SyndEntry>> {

    private static final Logger logger = LoggerFactory.getLogger(FeedItemReader.class);

    @Value("classpath:opml/engineering_blogs.opml")
    private Resource resourceOpml;

    private List<Outline> outlinesToRead;

    private Feed currentFeedToRead;

    private List<SyndEntry> currentEntriesToRead;

    private CollectorPersistingStore collectorPersistingStore;

    @PostConstruct
    public void init() {

        super.setTargetObject(this);
        super.setTargetMethod("read");

        collectorPersistingStore = CollectorPersistingStore.getInstance();


        WireFeedInput input = new WireFeedInput();
        List<Outline> outlines = null;
        try {
            Opml feed = (Opml) input.build(new InputSource(this.resourceOpml.getInputStream()));
            outlines = feed.getOutlines();
        } catch (IllegalArgumentException | IOException | FeedException exc) {
            logger.error("An error has been produced while opml was processed", exc);
        }

        this.outlinesToRead = !CollectionUtils.isEmpty(outlines) ? outlines.get(0)
                .getChildren()
                : Collections.emptyList();
    }

    @PreDestroy
    public void saveCurrentStateOfCollectorPersistingStore() {

        try {
            collectorPersistingStore.close();
        } catch (IOException e) {
            logger.error("Failed to persist the current state of CollectorPersistingStore.", e);
        }
    }

    @Override
    public Entry<Feed, SyndEntry> read() throws Exception {

        if (!CollectionUtils.isEmpty(this.currentEntriesToRead)) {
            return this.analyzeEntriesUpdatedToDate();

        } else if (!CollectionUtils.isEmpty(this.outlinesToRead)) {
            collectorPersistingStore.flush();
            initLoadOfCurrentEntriesToRead();
            return this.read();
        }

        collectorPersistingStore.flush();
        return null;
    }

    private void initLoadOfCurrentEntriesToRead() throws MalformedURLException {

        Outline outlineForFeed = this.outlinesToRead.remove(0);
        Entry<Feed, SyndFeed> feedWithOrigin = this.buildFeed(outlineForFeed.getText(),
                outlineForFeed.getXmlUrl());

        this.currentFeedToRead = feedWithOrigin.getKey();

        Optional.ofNullable(feedWithOrigin.getValue())
                .ifPresent(feed -> this.currentEntriesToRead = feed.getEntries()
                        .stream()
                        .sorted(Comparator.comparing(SyndEntry::getPublishedDate, nullsLast(naturalOrder()))
                                .thenComparing(Comparator.comparing(SyndEntry::getUpdatedDate, nullsLast(naturalOrder()))))
                        .collect(Collectors.toList()));
    }

    private Entry<Feed, SyndFeed> buildFeed(final String origin, final String feedUrl) throws MalformedURLException {

        SyndFeed feed = null;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpUriRequest request = new HttpGet(feedUrl);
            try (CloseableHttpResponse response = client.execute(request);
                 InputStream stream = response.getEntity()
                         .getContent()) {
                SyndFeedInput input = new SyndFeedInput();
                feed = input.build(new XmlReader(stream));
            }
        } catch (IllegalArgumentException | FeedException | IOException exc) {
            logger.error("Url of feed [{}]", feedUrl);
            logger.error("An error has been produced while opml was processed.", exc);
        }

        return Maps.immutableEntry(new Feed(new URL(feedUrl), origin.toUpperCase()), feed);
    }

    private Entry<Feed, SyndEntry> analyzeEntriesUpdatedToDate() {

        SyndEntry entryToRead = this.currentEntriesToRead.remove(0);

        try {

            Date entryDate = entryToRead.getPublishedDate() != null ? entryToRead.getPublishedDate()
                    : entryToRead.getUpdatedDate();

            if (collectorPersistingStore.checkEntryInfoWasStored(this.currentFeedToRead.getSource(), entryDate)) {
                // Don't clear entries to read list, because it's possible to find several feed
                // equals in the same iteration
                // this.currentEntriesToRead.clear();
                return this.read();
            } else {
                collectorPersistingStore.storeEntryInfo(this.currentFeedToRead.getSource(), entryDate);
            }

        } catch (Exception e) {
            logger.error("An error has been produced checking if entry was stored previously .", e);
        }

        return new AbstractMap.SimpleImmutableEntry<>(this.currentFeedToRead, entryToRead);
    }
}
