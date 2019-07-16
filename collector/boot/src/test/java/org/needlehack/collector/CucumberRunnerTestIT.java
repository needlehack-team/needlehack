package org.needlehack.collector;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/features"}, glue = {
        "org.needlehack.collector.steps"}, tags = {
        "~@suggester"}, features = "classpath:features", snippets = SnippetType.CAMELCASE)
public class CucumberRunnerTestIT {


}
