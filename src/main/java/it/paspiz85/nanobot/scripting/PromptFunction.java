package it.paspiz85.nanobot.scripting;

/**
 * Function to realize a prompt for user input.
 *
 * @author paspiz85
 *
 */
@FunctionalInterface
public interface PromptFunction {

    String apply(String msg, String defaultValue);
}
