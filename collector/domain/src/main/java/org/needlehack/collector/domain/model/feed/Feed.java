package org.needlehack.collector.domain.model.feed;

import java.io.Serializable;
import java.net.URL;
import java.util.Objects;

/**
 * Entity (part of FeedItem aggregate)
 */
public class Feed implements Serializable, Cloneable {

    public URL uri;

    public String source;

    public Feed(URL uri, String source) {
        this.uri = uri;
        this.source = source;
    }

    public URL getUri() {
        return uri;
    }

    public void setUri(URL uri) {
        this.uri = uri;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return Objects.equals(uri, feed.uri) &&
                Objects.equals(source, feed.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, source);
    }

    @Override
    public String toString() {
        return "Feed{" +
                "uri='" + uri + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public Object clone() {
        try {
            return (Feed) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Feed(this.getUri(), this.getSource());
        }
    }
}
