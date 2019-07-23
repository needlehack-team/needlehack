package org.needlehack.collector;

import org.needlehack.collector.infrastructure.runner.OpmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
public class FeedCollectorApplication implements CommandLineRunner {

    @Autowired
    OpmlReader opmlReader;

    private static final String DEFAULT_ENVIRONMENT = "local";

    public static void main(String[] args) {
        new SpringApplicationBuilder(FeedCollectorApplication.class).environment(new StandardEnvironment() {

            @Override
            public String[] getActiveProfiles() {
                final String systemEnvironmentVar = System.getenv("ENV");
                final String environmentStr = systemEnvironmentVar == null ? DEFAULT_ENVIRONMENT : systemEnvironmentVar;
                return new String[]{environmentStr};
            }
        })
                .run(args);
    }

    @Override
    public void run(String... args) {
        opmlReader.consumeOpml();
    }
}
