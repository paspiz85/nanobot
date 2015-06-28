package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.parsing.Clickable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for persist settings.
 *
 * @author paspiz85
 *
 */
public final class SettingsPersister implements Constants {

    private static final String PROPERTY_ATTACK_STRAT = "attack_strat";

    private static final String PROPERTY_BARRACKS_COORDS = "barracks_coords";

    private static final String PROPERTY_DE = "de";

    private static final String PROPERTY_DETECT_EMPTY_COLLECTORS = "detect_empty_collectors";

    private static final String PROPERTY_ELIXIR = "elixir";

    private static final String PROPERTY_GOLD = "gold";

    private static final String PROPERTY_IS_MATCH_ALL_CONDS = "match_all";

    private static final String PROPERTY_LOG_LEVEL = "log_level";

    private static final String PROPERTY_LOG_ENEMY_BASE = "log_enemy_base";

    private static final String PROPERTY_MAX_TH = "max_th";

    private static final String PROPERTY_PLAY_SOUND = "play_sound";

    private static final String PROPERTY_RAX_INFO = "rax_info";

    private File configFile;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private File getConfigFile() {
        if (configFile == null) {
            final String appdata = System.getenv("appdata");
            final File root = new File(appdata, ".");
            if (!root.isDirectory()) {
                root.mkdir();
            }
            configFile = new File(root, NAME.toLowerCase() + ".properties");
            if (!configFile.isFile()) {
                try {
                    configFile.createNewFile();
                } catch (final IOException e) {
                    logger.log(Level.SEVERE, "Unable to create configuration file.", e);
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
            final String playSoundProperty = configProperties.getProperty(PROPERTY_PLAY_SOUND);
            if (playSoundProperty != null) {
                settings.setPlaySound(Boolean.parseBoolean(playSoundProperty));
            }
            final String attackStratProperty = configProperties.getProperty(PROPERTY_ATTACK_STRAT);
            if (attackStratProperty != null) {
                boolean found = false;
                for (final Attack attack : Attack.getAvailableStrategies()) {
                    if (attack.getClass().getSimpleName().equals(attackStratProperty)) {
                        settings.setAttackStrategy(attack);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalArgumentException(attackStratProperty);
                }
            }
            final String raxInfoProperty = configProperties.getProperty(PROPERTY_RAX_INFO);
            if (raxInfoProperty != null) {
                settings.setRaxInfo(raxInfoProperty);
            }
            final String barracksCoordsProperty = configProperties.getProperty(PROPERTY_BARRACKS_COORDS);
            if (barracksCoordsProperty != null) {
                try (Scanner sc = new Scanner(barracksCoordsProperty)) {
                    final int x = sc.nextInt();
                    final int y = sc.nextInt();
                    settings.setFirstBarrackPosition(new Point(x, y));
                } catch (final Exception e) {
                    settings.setFirstBarrackPosition(null);
                }
            }
            final String logEnemyBase = configProperties.getProperty(PROPERTY_LOG_ENEMY_BASE);
            if (logEnemyBase != null) {
                settings.setLogEnemyBase(Boolean.parseBoolean(logEnemyBase));
            }
            final String logLevel = configProperties.getProperty(PROPERTY_LOG_LEVEL);
            if (logLevel != null) {
                settings.setLogLevel(Level.parse(logLevel));
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to read configuration file.", e);
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
            configProperties.setProperty(PROPERTY_PLAY_SOUND, String.valueOf(settings.isPlaySound()));
            configProperties.setProperty(PROPERTY_ATTACK_STRAT,
                    String.valueOf(settings.getAttackStrategy().getClass().getSimpleName()));
            final Point firstBarrackPosition = settings.getFirstBarrackPosition();
            if (firstBarrackPosition != null) {
                configProperties.setProperty(PROPERTY_BARRACKS_COORDS, firstBarrackPosition.x() + " "
                        + firstBarrackPosition.y());
            }
            final Clickable[] raxInfo = settings.getRaxInfo();
            final StringBuilder raxProp = new StringBuilder();
            for (int i = 0; i < raxInfo.length; i++) {
                final Clickable unit = raxInfo[i];
                if (i > 0) {
                    raxProp.append(", ");
                }
                raxProp.append(unit.getDescription());
            }
            configProperties.setProperty(PROPERTY_RAX_INFO, raxProp.toString());
            configProperties.setProperty(PROPERTY_LOG_ENEMY_BASE, String.valueOf(settings.isLogEnemyBase()));
            configProperties.setProperty(PROPERTY_LOG_LEVEL, String.valueOf(settings.getLogLevel()));
            configProperties.store(fos, null);
            logger.info("Settings are saved.");
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to save configuration file.", e);
        }
    }
}
