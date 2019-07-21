package org.needlehack.collector.infrastructure.crawl;

import org.needlehack.collector.infrastructure.crawl.CommonCrawler;
import org.needlehack.collector.infrastructure.crawl.ICrawler;
import org.needlehack.collector.infrastructure.repositories.ExtractorConfigJpaRepository;
import org.needlehack.collector.infrastructure.repositories.model.ExtractorConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class CrawlerFactory {

    @Autowired
    private ExtractorConfigJpaRepository extractorConfigRepository;

    public ICrawler getCrawler(final String origin) {

        ICrawler crawlerResult = null;
        Optional<ExtractorConfigEntity> extractorConfigOp =
                extractorConfigRepository.findByOriginIgnoreCase(origin);

        if (extractorConfigOp.isPresent()) {
            ExtractorConfigEntity extractorConfig = extractorConfigOp.get();
            crawlerResult = new CommonCrawler(extractorConfig.getSelectorContent(),
                    extractorConfig.getSelectorCategories());
        }

        return crawlerResult;
    }
}
