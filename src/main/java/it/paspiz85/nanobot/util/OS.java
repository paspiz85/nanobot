package it.paspiz85.nanobot.util;

import java.util.Locale;
import java.util.Scanner;

/**
 * Map operating systems.
 *
 * @author paspiz85
 *
 */
public final class OS {

    /**
     * Family of operating systems.
     *
     * @author paspiz85
     *
     */
    public static enum Family {
        MAC, SOLARIS, UNIX, UNKNOW, WINDOWS
    }

    /**
     * Version of operating systems.
     *
     * @author paspiz85
     *
     */
    public static class Version {

        private final String version;

        private final int major;

        private final int minor;

        Version(final String version) {
            this.version = version;
            try (Scanner scanner = new Scanner(version)) {
                scanner.useDelimiter("\\D");
                this.major = scanner.nextInt();
                this.minor = scanner.nextInt();
            }
        }

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }

        @Override
        public String toString() {
            return version;
        }
    }

    public static OS getCurrent() {
        return Utils.singleton(OS.class, () -> new OS());
    }

    private static Family getFamily(final OS os) {
        Family result = Family.UNKNOW;
        final String name = os.name.toLowerCase(Locale.ROOT);
        if (name.indexOf("win") >= 0) {
            result = Family.WINDOWS;
        } else if (name.indexOf("mac") >= 0) {
            result = Family.MAC;
        } else if (name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0 || name.indexOf("aix") > 0) {
            result = Family.UNIX;
        } else if (name.indexOf("sunos") >= 0) {
            result = Family.SOLARIS;
        }
        return result;
    }

    private final String arch;

    private final String name;

    private final Version version;

    private final Family family;

    private OS() {
        this(System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
    }

    private OS(final String name, final String version, final String arch) {
        this.name = name;
        this.version = new Version(version);
        this.arch = arch;
        this.family = getFamily(this);
    }

    public String getArch() {
        return arch;
    }

    public Family getFamily() {
        return family;
    }

    public String getName() {
        return name;
    }

    public Version getVersion() {
        return version;
    }
}
