package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.parsing.Clickable;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SettingsPersister implements Constants {

    private static final String PROPERTY_ATTACK_STRAT = "attack_strat";

    private static final String PROPERTY_BARRACKS_COORDS = "barracks_coords";

    private static final String PROPERTY_DE = "de";

    private static final String PROPERTY_DETECT_EMPTY_COLLECTORS = "detect_empty_collectors";

    private static final String PROPERTY_ELIXIR = "elixir";

    private static final String PROPERTY_GOLD = "gold";

    private static final String PROPERTY_IS_MATCH_ALL_CONDS = "match_all";

    private static final String PROPERTY_LOG_ENEMY_BASE = "log_enemy_base";

    private static final String PROPERTY_MAX_TH = "max_th";

    private static final String PROPERTY_PLAY_SOUND = "play_sound";

    private static final String PROPERTY_RAX_INFO = "rax_info";

    @Deprecated
    private File configFile;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private File getConfigFile() {
        if (configFile == null) {
            String appdata = System.getenv("appdata");
            File root = new File(appdata, ".");
            if (!root.isDirectory()) {
                root.mkdir();
            }
            configFile = new File(root, NAME.toLowerCase() + ".properties");
            if (!configFile.isFile()) {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Unable to create configuration file.", e);
                }
            }
        }
        return configFile;
    }

    public void reload(Settings settings) {
        Properties configProperties = new Properties();
        try (InputStream is = new FileInputStream(getConfigFile())) {
            configProperties.load(is);
            String goldProperty = configProperties.getProperty(PROPERTY_GOLD);
            if (goldProperty != null) {
                settings.setGoldThreshold(Integer.parseInt(goldProperty));
            }
            String elixirProperty = configProperties.getProperty(PROPERTY_ELIXIR);
            if (elixirProperty != null) {
                settings.setElixirThreshold(Integer.parseInt(elixirProperty));
            }
            String deProperty = configProperties.getProperty(PROPERTY_DE);
            if (deProperty != null) {
                settings.setDarkElixirThreshold(Integer.parseInt(deProperty));
            }
            String maxThProperty = configProperties.getProperty(PROPERTY_MAX_TH);
            if (maxThProperty != null) {
                settings.setMaxThThreshold(Integer.parseInt(maxThProperty));
            }
            String matchAllCondsProperty = configProperties.getProperty(PROPERTY_IS_MATCH_ALL_CONDS);
            if (matchAllCondsProperty != null) {
                settings.setMatchAllConditions(Boolean.parseBoolean(matchAllCondsProperty));
            }
            String detectEmptyCollectorsProperty = configProperties.getProperty(PROPERTY_DETECT_EMPTY_COLLECTORS);
            if (detectEmptyCollectorsProperty != null) {
                settings.setDetectEmptyCollectors(Boolean.parseBoolean(detectEmptyCollectorsProperty));
            }
            String playSoundProperty = configProperties.getProperty(PROPERTY_PLAY_SOUND);
            if (playSoundProperty != null) {
                settings.setPlaySound(Boolean.parseBoolean(playSoundProperty));
            }
            String attackStratProperty = configProperties.getProperty(PROPERTY_ATTACK_STRAT);
            if (attackStratProperty != null) {
                settings.setAttackStrategy(attackStratProperty);
            }
            String raxInfoProperty = configProperties.getProperty(PROPERTY_RAX_INFO);
            if (raxInfoProperty != null) {
                settings.setRaxInfo(raxInfoProperty);
            }
            String barracksCoordsProperty = configProperties.getProperty(PROPERTY_BARRACKS_COORDS);
            if (barracksCoordsProperty != null) {
                try (Scanner sc = new Scanner(barracksCoordsProperty)) {
                    int x = sc.nextInt();
                    int y = sc.nextInt();
                    settings.setFirstBarrackPosition(new Point(x, y));
                } catch (Exception e) {
                    settings.setFirstBarrackPosition(null);
                }
            }
            String logEnemyBase = configProperties.getProperty(PROPERTY_LOG_ENEMY_BASE);
            if (logEnemyBase != null) {
                settings.setLogEnemyBase(Boolean.parseBoolean(logEnemyBase));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to read configuration file.", e);
        }
    }

    public void save(Settings settings) {
        try (FileOutputStream fos = new FileOutputStream(getConfigFile())) {
            Properties configProperties = new Properties();
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
            Point firstBarrackPosition = settings.getFirstBarrackPosition();
            if (firstBarrackPosition != null) {
                configProperties.setProperty(PROPERTY_BARRACKS_COORDS, (int) firstBarrackPosition.getX() + " "
                        + (int) firstBarrackPosition.getY());
            }
            Clickable[] raxInfo = settings.getRaxInfo();
            StringBuilder raxProp = new StringBuilder();
            for (int i = 0; i < raxInfo.length; i++) {
                Clickable unit = raxInfo[i];
                if (i > 0) {
                    raxProp.append(", ");
                }
                raxProp.append(unit.getDescription());
            }
            configProperties.setProperty(PROPERTY_RAX_INFO, raxProp.toString());
            configProperties.setProperty(PROPERTY_LOG_ENEMY_BASE, String.valueOf(settings.isLogEnemyBase()));
            configProperties.store(fos, null);
            logger.info("Settings are saved.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to save configuration file.", e);
        }
    }
}
