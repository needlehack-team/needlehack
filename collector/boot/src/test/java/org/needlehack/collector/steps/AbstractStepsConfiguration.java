package org.needlehack.collector.steps;

import org.needlehack.collector.FeedCollectorApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = FeedCollectorApplication.class, loader = SpringBootContextLoader.class)
@ActiveProfiles("test")
public class AbstractStepsConfiguration {


}
