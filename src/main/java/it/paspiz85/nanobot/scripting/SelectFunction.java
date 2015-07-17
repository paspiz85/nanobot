package it.paspiz85.nanobot.scripting;

/**
 * Function to realize a selection between a collection of values.
 *
 * @author paspiz85
 *
 */
@FunctionalInterface
public interface SelectFunction {

    String apply(String msg, String[] options);
}
