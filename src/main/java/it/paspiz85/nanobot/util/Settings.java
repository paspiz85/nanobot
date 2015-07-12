package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.parsing.TroopButton;

import java.util.logging.Level;

/**
 * Bot settings.
 *
 * @author paspiz85
 *
 */
public final class Settings {

    private static final Settings INSTANCE = new Settings();

    public static synchronized void close() {
        if (!INSTANCE.isInitialized) {
            throw new IllegalStateException("ConfigUtils is not initialized.");
        }
        INSTANCE.isInitialized = false;
    }

    public static synchronized void initialize() throws IllegalStateException {
        // Throw exception if called twice
        if (INSTANCE.isInitialized) {
            throw new IllegalStateException("ConfigUtils is already initialized.");
        }
        INSTANCE.configPersister.reload(INSTANCE);
        INSTANCE.isInitialized = true;
    }

    /**
     * Singleton accessor method.
     */
    public static Settings instance() {
        if (!INSTANCE.isInitialized) {
            synchronized (Settings.class) {
                if (!INSTANCE.isInitialized) {
                    throw new IllegalStateException("ConfigUtils is not initialized.");
                }
            }
        }
        return INSTANCE;
    }

    public static boolean isInitialized() {
        return INSTANCE.isInitialized;
    }

    private Attack attackStrategy = Attack.noStrategy();

    private final TroopButton[] availableTroops;

    private boolean collectResources = true;

    private final SettingsPersister configPersister = new SettingsPersister();

    private int darkElixirThreshold;

    private boolean detectEmptyCollectors = true;

    private int elixirThreshold = 100000;

    private int goldThreshold = 100000;

    private boolean isInitialized;

    private Level logLevel = Level.INFO;

    private boolean matchAllConditions;

    private int maxThThreshold;

    private final TroopButton[] raxInfo = new TroopButton[] { TroopButton.BARB, TroopButton.BARB, TroopButton.ARCHER,
            TroopButton.ARCHER, TroopButton.NO_UNIT, TroopButton.NO_UNIT };

    private boolean trainTroops = true;

    private Settings() {
        availableTroops = new TroopButton[] { TroopButton.NO_UNIT, TroopButton.BARB, TroopButton.ARCHER,
                TroopButton.GIANT, TroopButton.GOBLIN, TroopButton.WB, TroopButton.BALLOON, TroopButton.WIZARD,
                TroopButton.HEALER, TroopButton.DRAGON, TroopButton.PEKKA, TroopButton.MINION, TroopButton.HOGRIDER };
    }

    public Attack getAttackStrategy() {
        return this.attackStrategy;
    }

    public TroopButton[] getAvailableTroops() {
        return availableTroops;
    }

    public int getDarkElixirThreshold() {
        return darkElixirThreshold;
    }

    public int getElixirThreshold() {
        return elixirThreshold;
    }

    public int getGoldThreshold() {
        return goldThreshold;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public int getMaxThThreshold() {
        return maxThThreshold;
    }

    public TroopButton[] getRaxInfo() {
        return raxInfo;
    }

    public boolean isCollectResources() {
        return collectResources;
    }

    public boolean isDetectEmptyCollectors() {
        return detectEmptyCollectors;
    }

    public boolean isMatchAllConditions() {
        return matchAllConditions;
    }

    public boolean isTrainTroops() {
        return trainTroops;
    }

    public void save() {
        configPersister.save(this);
    }

    public void setAttackStrategy(final Attack attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public void setCollectResources(final boolean collectResources) {
        this.collectResources = collectResources;
    }

    public void setDarkElixirThreshold(final int darkElixirThreshold) {
        this.darkElixirThreshold = darkElixirThreshold;
    }

    public void setDetectEmptyCollectors(final boolean detectEmptyCollectors) {
        this.detectEmptyCollectors = detectEmptyCollectors;
    }

    public void setElixirThreshold(final int elixirThreshold) {
        this.elixirThreshold = elixirThreshold;
    }

    public void setGoldThreshold(final int goldThreshold) {
        this.goldThreshold = goldThreshold;
    }

    public void setLogLevel(final Level logLevel) {
        this.logLevel = logLevel;
    }

    public void setMatchAllConditions(final boolean matchAllConditions) {
        this.matchAllConditions = matchAllConditions;
    }

    public void setMaxThThreshold(final int maxThThreshold) {
        this.maxThThreshold = maxThThreshold;
    }

    public void setRaxInfo(final String raxInfoProperty) {
        final int raxCount = raxInfo.length;
        final String[] splits = raxInfoProperty.split("\\s*,\\s*");
        for (int i = 0; i < splits.length && i < raxCount; i++) {
            final String split = splits[i];
            raxInfo[i] = TroopButton.fromDescription(split);
        }
    }

    public void setTrainTroops(final boolean trainTroops) {
        this.trainTroops = trainTroops;
    }
}
