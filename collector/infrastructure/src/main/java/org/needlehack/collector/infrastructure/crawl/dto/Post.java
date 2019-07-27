package org.needlehack.collector.infrastructure.crawl.dto;

import java.util.List;
import java.util.Set;

public class Post {

    private String content;

    private String link;

    /*
     * Output field from disambiguate process to specify technical tags
     */
    private Set<String> tags;

    /*
     * Output field from disambiguate process to specify description labels
     */
    private Set<String> labels;

    /*
     * Input field in disambiguate process
     */
    private List<String> markers;

    private Post(String content, String link, Set<String> tags, Set<String> labels, List<String> markers) {
        this.content = content;
        this.link = link;
        this.tags = tags;
        this.labels = labels;
        this.markers = markers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    public List<String> getMarkers() {
        return markers;
    }

    public void setMarkers(List<String> markers) {
        this.markers = markers;
    }

    @Override
    public String toString() {
        return "Post{" +
                "content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", tags=" + tags +
                ", labels=" + labels +
                ", markers=" + markers +
                '}';
    }

    public static class PostBuilder {

        private String content;
        private String link;
        private Set<String> tags;
        private Set<String> labels;
        private List<String> markers;

        public PostBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public PostBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public PostBuilder withTags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        public PostBuilder withLabels(Set<String> labels) {
            this.labels = labels;
            return this;
        }

        public PostBuilder withMarkers(List<String> markers) {
            this.markers = markers;
            return this;
        }

        public Post createPost() {
            return new Post(content, link, tags, labels, markers);
        }
    }
}
