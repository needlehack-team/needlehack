package org.needlehack.collector.infrastructure.crawl;

import org.needlehack.collector.infrastructure.crawl.dto.Post;

public class CommonCrawler extends PostCrawler {

    private String selectorContent;
    private String selectorCategories;

    public CommonCrawler(String selectorContent, String selectorCategories) {
        this.selectorContent = selectorContent;
        this.selectorCategories = selectorCategories;
    }

    @Override
    public Post retrievePostByUrl(String urlPageFrom) {
        return retrievePost(urlPageFrom, this.selectorContent, this.selectorCategories);
    }
}
