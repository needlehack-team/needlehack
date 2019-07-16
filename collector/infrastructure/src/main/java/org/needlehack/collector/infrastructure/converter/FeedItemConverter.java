package org.needlehack.collector.infrastructure.converter;

import com.rometools.rome.feed.synd.SyndEntry;
import org.needlehack.collector.domain.model.feed.Feed;
import org.needlehack.collector.domain.model.feed.FeedItem;
import org.needlehack.collector.domain.model.feed.Topic;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class FeedItemConverter {

    private FeedItemConverter() {
        throw new IllegalStateException("Private access to constructor");
    }

    public static FeedItem convertFeedToPost(final Feed origin, final SyndEntry entry) {

        FeedItem.FeedItemBuilder builder = new FeedItem.FeedItemBuilder();

        builder.withTitle(entry.getTitle())
                .withUri(entry.getLink())
                .withCreator(entry.getAuthor())
                .withOrigin(origin);

        builder.withPublicationAt(Optional.ofNullable(entry.getPublishedDate())
                .orElse(entry.getUpdatedDate()));

        builder.withCollectAt(Date.from(Instant.now()));

        if (!CollectionUtils.isEmpty(entry.getContents())) {
            StringBuilder strBuilder = new StringBuilder();
            entry.getContents()
                    .forEach(content -> strBuilder.append(content.getValue()));
            builder.withContent(strBuilder.toString());
        }

        if (!CollectionUtils.isEmpty(entry.getCategories())) {
            Set<Topic> topicCollection = entry.getCategories()
                    .stream()
                    .map(syndCategory -> new Topic(syndCategory.getName()))
                    .collect(Collectors.toSet());
            builder.withTopics(topicCollection);
        }

        return builder.createFeedItem();
    }

}
