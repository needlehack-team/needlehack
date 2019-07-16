package org.needlehack.collector.infrastructure.batch.reader.dto;

import java.util.Date;

public class FeedLastInfo {

    private String origin;

    private Date publicationTimestamp;

    public FeedLastInfo() {
    }

    public FeedLastInfo(String origin, Date publicationTimestamp) {
        this.origin = origin;
        this.publicationTimestamp = publicationTimestamp;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getPublicationTimestamp() {
        return publicationTimestamp;
    }

    public void setPublicationTimestamp(Date publicationTimestamp) {
        this.publicationTimestamp = publicationTimestamp;
    }

    @Override
    public String toString() {
        return "FeedLastInfo{" +
                "origin='" + origin + '\'' +
                ", publicationTimestamp=" + publicationTimestamp +
                '}';
    }
}
