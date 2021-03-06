package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.game.Troop;
import it.paspiz85.nanobot.platform.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for persist settings.
 *
 * @author paspiz85
 *
 */
public final class SettingsPersister {

    private static final String PROPERTY_ATTACK_STRAT = "attack_strat";

    private static final String PROPERTY_COLLECT_RESOURCES = "collect_resources";

    private static final String PROPERTY_DE = "de";

    private static final String PROPERTY_DETECT_EMPTY_COLLECTORS = "detect_empty_collectors";

    private static final String PROPERTY_ELIXIR = "elixir";

    private static final String PROPERTY_EXTRA_FUNCTIONS = "extra_functions";

    private static final String PROPERTY_GOLD = "gold";

    private static final String PROPERTY_IS_MATCH_ALL_CONDS = "match_all";

    private static final String PROPERTY_LOG_LEVEL = "log_level";

    private static final String PROPERTY_MAX_TH = "max_th";

    private static final String PROPERTY_RAX_INFO = "rax_info";

    private static final String PROPERTY_TRAIN_MAX_TROOPS = "train_max_troops";

    private static final String PROPERTY_USER_MAIL_ADDRESS = "user_mail_address";

    private static final String PROPERTY_PREFERRED_PLATFORM = "preferred_platform";

    private static final String PROPERTY_UUID = "uuid";

    private File configFile;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private File getConfigFile() {
        if (configFile == null) {
            final String appdata = System.getenv("appdata");
            final File root = new File(appdata, ".");
            if (!root.isDirectory()) {
                root.mkdir();
            }
            configFile = new File(root, BuildInfo.instance().getName().toLowerCase() + ".properties");
            if (!configFile.isFile()) {
                try {
                    configFile.createNewFile();
                } catch (final IOException e) {
                    logger.log(Level.SEVERE, "Unable to create configuration file", e);
                }
            }
        }
        return configFile;
    }

    public void reload(final Settings settings) {
        final Properties configProperties = new Properties();
        try (InputStream is = new FileInputStream(getConfigFile())) {
            configProperties.load(is);
            final String goldProperty = configProperties.getProperty(PROPERTY_GOLD);
            if (goldProperty != null) {
                settings.setGoldThreshold(Integer.parseInt(goldProperty));
            }
            final String elixirProperty = configProperties.getProperty(PROPERTY_ELIXIR);
            if (elixirProperty != null) {
                settings.setElixirThreshold(Integer.parseInt(elixirProperty));
            }
            final String deProperty = configProperties.getProperty(PROPERTY_DE);
            if (deProperty != null) {
                settings.setDarkElixirThreshold(Integer.parseInt(deProperty));
            }
            final String maxThProperty = configProperties.getProperty(PROPERTY_MAX_TH);
            if (maxThProperty != null) {
                settings.setMaxThThreshold(Integer.parseInt(maxThProperty));
            }
            final String matchAllCondsProperty = configProperties.getProperty(PROPERTY_IS_MATCH_ALL_CONDS);
            if (matchAllCondsProperty != null) {
                settings.setMatchAllConditions(Boolean.parseBoolean(matchAllCondsProperty));
            }
            final String detectEmptyCollectorsProperty = configProperties.getProperty(PROPERTY_DETECT_EMPTY_COLLECTORS);
            if (detectEmptyCollectorsProperty != null) {
                settings.setDetectEmptyCollectors(Boolean.parseBoolean(detectEmptyCollectorsProperty));
            }
            final String collectResourcesProperty = configProperties.getProperty(PROPERTY_COLLECT_RESOURCES);
            if (collectResourcesProperty != null) {
                settings.setCollectResources(Boolean.parseBoolean(collectResourcesProperty));
            }
            final String trainMaxTroopsProperty = configProperties.getProperty(PROPERTY_TRAIN_MAX_TROOPS);
            if (trainMaxTroopsProperty != null) {
                settings.setTrainMaxTroops(Integer.parseInt(trainMaxTroopsProperty));
            }
            final String uuidProperty = configProperties.getProperty(PROPERTY_UUID);
            if (uuidProperty != null) {
                settings.setUuid(UUID.fromString(uuidProperty));
            }
            final String userMailAddressProperty = configProperties.getProperty(PROPERTY_USER_MAIL_ADDRESS);
            if (userMailAddressProperty != null) {
                settings.setUserMailAddress(userMailAddressProperty);
            }
            final String preferredPlatformProperty = configProperties.getProperty(PROPERTY_PREFERRED_PLATFORM);
            if (preferredPlatformProperty != null && !preferredPlatformProperty.isEmpty()) {
                try {
                    @SuppressWarnings("unchecked")
                    final Class<? extends Platform> preferredPlatform = (Class<? extends Platform>) Class
                            .forName(preferredPlatformProperty);
                    settings.setPreferredPlatform(preferredPlatform);
                } catch (final ClassNotFoundException e1) {
                    logger.log(Level.SEVERE, "Platform not found: " + preferredPlatformProperty);
                }
            }
            final String attackStratProperty = configProperties.getProperty(PROPERTY_ATTACK_STRAT);
            if (attackStratProperty != null) {
                settings.setAttackStrategy(attackStratProperty);
            }
            final String raxInfoProperty = configProperties.getProperty(PROPERTY_RAX_INFO);
            if (raxInfoProperty != null) {
                settings.setRaxInfo(raxInfoProperty);
            }
            final String logLevel = configProperties.getProperty(PROPERTY_LOG_LEVEL);
            if (logLevel != null) {
                settings.setLogLevel(Level.parse(logLevel));
            }
            final String extraFunctionsProperty = configProperties.getProperty(PROPERTY_EXTRA_FUNCTIONS);
            if (extraFunctionsProperty != null) {
                settings.setExtraFunctions(Boolean.parseBoolean(extraFunctionsProperty));
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to read configuration file", e);
        }
    }

    public void save(final Settings settings) {
        try (FileOutputStream fos = new FileOutputStream(getConfigFile())) {
            final Properties configProperties = new Properties();
            configProperties.setProperty(PROPERTY_GOLD, String.valueOf(settings.getGoldThreshold()));
            configProperties.setProperty(PROPERTY_ELIXIR, String.valueOf(settings.getElixirThreshold()));
            configProperties.setProperty(PROPERTY_DE, String.valueOf(settings.getDarkElixirThreshold()));
            configProperties.setProperty(PROPERTY_MAX_TH, String.valueOf(settings.getMaxThThreshold()));
            configProperties.setProperty(PROPERTY_IS_MATCH_ALL_CONDS, String.valueOf(settings.isMatchAllConditions()));
            configProperties.setProperty(PROPERTY_DETECT_EMPTY_COLLECTORS,
                    String.valueOf(settings.isDetectEmptyCollectors()));
            configProperties.setProperty(PROPERTY_COLLECT_RESOURCES, String.valueOf(settings.isCollectResources()));
            configProperties.setProperty(PROPERTY_TRAIN_MAX_TROOPS, String.valueOf(settings.getTrainMaxTroops()));
            configProperties.setProperty(PROPERTY_UUID, settings.getUuid().toString());
            configProperties.setProperty(PROPERTY_USER_MAIL_ADDRESS, settings.getUserMailAddress());
            configProperties.setProperty(PROPERTY_ATTACK_STRAT, settings.getAttackStrategy());
            final Troop[] raxInfo = settings.getRaxInfo();
            final StringBuilder raxProp = new StringBuilder();
            for (int i = 0; i < raxInfo.length; i++) {
                final Troop unit = raxInfo[i];
                if (i > 0) {
                    raxProp.append(", ");
                }
                raxProp.append(unit.getDescription());
            }
            configProperties.setProperty(PROPERTY_RAX_INFO, raxProp.toString());
            configProperties.setProperty(PROPERTY_LOG_LEVEL, String.valueOf(settings.getLogLevel()));
            final Class<? extends Platform> preferredPlatform = settings.getPreferredPlatform();
            if (preferredPlatform != null) {
                configProperties.setProperty(PROPERTY_PREFERRED_PLATFORM, preferredPlatform.getName());
            }
            configProperties.setProperty(PROPERTY_EXTRA_FUNCTIONS, String.valueOf(settings.isExtraFunctions()));
            configProperties.store(fos, null);
            logger.log(Level.INFO, "Settings are saved");
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to save configuration file", e);
        }
    }
}
