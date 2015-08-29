package it.paspiz85.nanobot.test;

import it.paspiz85.nanobot.test.FeaturesConfig.Tag;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "features" }, glue = { FeaturesConfig.GLUE }, strict = true, plugin = { "pretty" }, tags = {
        Tag.IT, Tag.NO_WIP, Tag.NO_DEPRECATED })
public class ExtraFeaturesIT {
}
