package org.needlehack.collector.infrastructure.crawl.dto;

import java.util.Set;

public class CrawlSection {

    private String content;

    private Set<String> tags;

    public CrawlSection(String content, Set<String> tags) {
        this.content = content;
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
