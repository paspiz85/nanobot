package it.paspiz85.nanobot.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * Build information retriever.
 *
 * @author paspiz85
 *
 */
public final class BuildInfo implements Constants {

    private static final String DONATE_PATH = "#donate";

    private static final String RELEASES_LATEST_PATH = "/releases/latest";

    private static final String DONATE_URL = "http://rawgit.com/paspiz85/nanobot/master/donate.html";

    private static final String GITHUB_URL = "https://github.com/";

    private static BuildInfo instance;

    private static final String REPOSITORY_NAME = "paspiz85/nanobot";

    public static BuildInfo instance() {
        if (instance == null) {
            instance = new BuildInfo();
        }
        return instance;
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Manifest manifest;

    private BuildInfo() {
        Manifest manifest = null;
        try {
            final Enumeration<URL> resources = getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (resources.hasMoreElements()) {
                try (InputStream in = resources.nextElement().openStream()) {
                    final Manifest man = new Manifest(in);
                    if (NAME.toLowerCase().equals(man.getMainAttributes().getValue("Build-Name"))) {
                        manifest = man;
                        break;
                    }
                }
            }
        } catch (final IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        this.manifest = manifest;
    }

    public String checkForUpdate() {
        String result = null;
        final String current = getVersion();
        final String release = getLatestVersion();
        if (current != null && release != null) {
            final DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(current);
            final DefaultArtifactVersion releaseVersion = new DefaultArtifactVersion(release);
            if (currentVersion.compareTo(releaseVersion) < 0) {
                result = release;
            }
        }
        return result;
    }

    public String getDonateUrl() {
        return DONATE_URL;
    }

    public String getLatestVersion() {
        String result = null;
        try {
            final GitHub github = GitHub.connectAnonymously();
            final GHRepository repository = github.getRepository(REPOSITORY_NAME);
            for (final GHRelease r : repository.listReleases()) {
                result = r.getName().substring(1);
            }
        } catch (final IOException e) {
            logger.log(Level.WARNING, "Unable to get latest version", e);
        }
        return result;
    }

    public String getLatestVersionUrl() {
        return GITHUB_URL + REPOSITORY_NAME + RELEASES_LATEST_PATH;
    }

    private String getManifestAttribute(final String name) {
        String result = null;
        if (manifest != null) {
            result = manifest.getMainAttributes().getValue(name);
        }
        return result;
    }

    public String getName() {
        String buildName;
        final String version = getVersion();
        if (version == null) {
            buildName = "Development build";
        } else {
            buildName = NAME + " v" + version;
        }
        return buildName;
    }

    public String getRepositoryUrl() {
        return GITHUB_URL + REPOSITORY_NAME;
    }

    public String getTimestamp() {
        return getManifestAttribute("Build-Timestamp");
    }

    public String getVersion() {
        return getManifestAttribute("Build-Version");
    }
}
