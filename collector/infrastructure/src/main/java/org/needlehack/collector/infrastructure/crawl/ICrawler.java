package org.needlehack.collector.infrastructure.crawl;

import org.needlehack.collector.infrastructure.crawl.dto.CrawlSection;

@FunctionalInterface
public interface ICrawler {

    CrawlSection crawl(String link);
}