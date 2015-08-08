package it.paspiz85.nanobot.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "features" }, glue = { "it.paspiz85.nanobot.test" }, strict = true, plugin = { "pretty" }, tags = {
        FeatureTags.TEST, FeatureTags.NO_WIP, FeatureTags.NO_DEPRECATED })
public class ExtraFeaturesTest {
}
