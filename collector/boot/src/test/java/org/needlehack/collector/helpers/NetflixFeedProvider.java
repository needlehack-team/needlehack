package org.needlehack.collector.helpers;

import com.rometools.rome.feed.synd.*;

import java.util.ArrayList;
import java.util.List;

public final class NetflixFeedProvider {

    public static SyndFeed getFeed(final String link) throws Exception {
        SyndFeed feed = new SyndFeedImpl();
        feed.setEncoding("UTF-8");
        feed.setTitle("Netflix TechBlog - Medium");
        feed.setLink(link);
        feed.setDescription("Learn about Netflix’s world class engineering efforts, company culture, product developments and more. - Medium");
        feed.setEntries(getFeedEntries());
        return feed;
    }

    /**
     *
     */
    private static List<SyndEntry> getFeedEntries() {
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        entries.add(getFeedEntry());
        return entries;
    }

    /**
     *
     */
    private static SyndEntry getFeedEntry() {

        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Building and Scaling Data Lineage at Netflix to Improve Data Infrastructure Reliability, and…");
        entry.setAuthor("Netflix Technology Blog");
        entry.setLink("https://medium.com/netflix-techblog/building-and-scaling-data-lineage-at-netflix-to-improve-data-infrastructure-reliability-and-1a52526a7977?source=rss----2615bd06b42e---4");
        entry.setUri("https://medium.com/p/1a52526a7977");
        return entry;
    }
}
