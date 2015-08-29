package it.paspiz85.nanobot.test;

public class FeaturesConfig {

    public interface Tag {

        String IT = "@integration-test";

        String NO_DEPRECATED = "~@deprecated";

        String NO_WIP = "~@wip";

        String TDD = "@tdd";

        String TEST = "@unit-test";

        String WIP = "@wip";
    }

    public static final String GLUE = "it.paspiz85.nanobot.test";
}
