package it.paspiz85.nanobot.util;

import java.util.Locale;

/**
 * Map operating systems.
 *
 * @author paspiz85
 *
 */
public final class OS {

    /**
     * Category of operating systems.
     *
     * @author paspiz85
     *
     */
    public enum Category {
        MAC, SOLARIS, UNIX, UNKNOW, WINDOWS
    }

    public static Category getCategory() {
        Category result = Category.UNKNOW;
        final String name = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (name.indexOf("win") >= 0) {
            result = Category.WINDOWS;
        } else if (name.indexOf("mac") >= 0) {
            result = Category.MAC;
        } else if (name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") > 0) {
            result = Category.UNIX;
        } else if (name.indexOf("sunos") >= 0) {
            result = Category.SOLARIS;
        }
        return result;
    }

    private OS() {
    }
}
