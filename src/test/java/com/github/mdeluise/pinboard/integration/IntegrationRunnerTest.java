package com.github.mdeluise.pinboard.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",
    glue = {
        "com.github.mdeluise.pinboard.integration",
        "com.github.mdeluise.pinboard.integration.steps"
    },
    plugin = {"pretty"}
)
public class IntegrationRunnerTest {
}
