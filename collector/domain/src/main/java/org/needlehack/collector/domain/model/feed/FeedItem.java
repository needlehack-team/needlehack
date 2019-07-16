package org.needlehack.collector.domain.model.feed;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class FeedItem implements Serializable, Cloneable {

    private static final long serialVersionUID = -5898378247168511380L;

    private String id;

    private String generatedId;

    private String title;

    private String uri;

    private String creator;

    private Feed origin;

    private String content;

    private Date collectAt;

    private Date publicationAt;

    private Set<Topic> topics;

    public FeedItem() {
    }

    private FeedItem(String id, String title, String uri, String creator, Feed origin, String content, Date collectAt, Date publicationAt, Set<Topic> topics) {
        this.generatedId = generateId(uri);
        this.title = title;
        this.uri = uri;
        this.creator = creator;
        this.origin = origin;
        this.content = content;
        this.collectAt = collectAt;
        this.publicationAt = publicationAt;
        this.topics = topics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Feed getOrigin() {
        return origin;
    }

    public void setOrigin(Feed origin) {
        this.origin = origin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCollectAt() {
        return collectAt;
    }

    public void setCollectAt(Date collectAt) {
        this.collectAt = collectAt;
    }

    public Date getPublicationAt() {
        return publicationAt;
    }

    public void setPublicationAt(Date publicationAt) {
        this.publicationAt = publicationAt;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedItem feedItem = (FeedItem) o;
        return Objects.equals(title, feedItem.title) &&
                Objects.equals(uri, feedItem.uri) &&
                Objects.equals(creator, feedItem.creator) &&
                Objects.equals(origin, feedItem.origin) &&
                Objects.equals(content, feedItem.content) &&
                Objects.equals(collectAt, feedItem.collectAt) &&
                Objects.equals(publicationAt, feedItem.publicationAt) &&
                Objects.equals(topics, feedItem.topics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, uri, creator, origin, content, collectAt, publicationAt, topics);
    }

    @Override
    public String toString() {
        return "FeedItem{" +
                "title='" + title + '\'' +
                ", uri='" + uri + '\'' +
                ", creator='" + creator + '\'' +
                ", origin=" + origin +
                ", content='" + content + '\'' +
                ", collectAt=" + collectAt +
                ", publicationAt=" + publicationAt +
                ", topics=" + topics +
                '}';
    }

    public String generateId(final String uri) {

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(uri.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest)
                .toUpperCase();
    }

    @Override
    public Object clone() {
        try {
            return (FeedItem) super.clone();
        } catch (CloneNotSupportedException e) {
            return new FeedItem(this.getId(), this.getTitle(), this.getUri(), this.getCreator(), this.getOrigin(),
                    this.getContent(), this.getCollectAt(), this.getPublicationAt(), this.getTopics());
        }
    }

    public static class FeedItemBuilder {
        private String title;
        private String uri;
        private String creator;
        private Feed origin;
        private String content;
        private Date collectAt;
        private Date publicationAt;
        private Set<Topic> topics;

        public FeedItemBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public FeedItemBuilder withUri(String uri) {
            this.uri = uri;
            return this;
        }

        public FeedItemBuilder withCreator(String creator) {
            this.creator = creator;
            return this;
        }

        public FeedItemBuilder withOrigin(Feed origin) {
            this.origin = origin;
            return this;
        }

        public FeedItemBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public FeedItemBuilder withCollectAt(Date collectAt) {
            this.collectAt = collectAt;
            return this;
        }

        public FeedItemBuilder withPublicationAt(Date publicationAt) {
            this.publicationAt = publicationAt;
            return this;
        }

        public FeedItemBuilder withTopics(Set<Topic> topics) {
            this.topics = topics;
            return this;
        }

        public FeedItem createFeedItem() {
            return new FeedItem(null, title, uri, creator, origin, content, collectAt, publicationAt, topics);
        }
    }
}
