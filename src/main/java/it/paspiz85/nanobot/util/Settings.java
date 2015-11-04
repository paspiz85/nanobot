package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.parsing.TroopButton;
import it.paspiz85.nanobot.platform.Platform;

import java.util.UUID;
import java.util.logging.Level;

/**
 * Bot settings.
 *
 * @author paspiz85
 *
 */
public final class Settings {

    private static final Settings INSTANCE = new Settings();

    public static final int MAX_TRAIN_TROOPS = 240;

    public static synchronized void close() {
        if (!INSTANCE.isInitialized) {
            throw new IllegalStateException(Settings.class.getSimpleName() + " is not initialized");
        }
        INSTANCE.isInitialized = false;
    }

    /**
     * Singleton accessor method.
     */
    public static synchronized Settings instance() {
        if (!INSTANCE.isInitialized) {
            INSTANCE.configPersister.reload(INSTANCE);
            INSTANCE.isInitialized = true;
        }
        return INSTANCE;
    }

    private String attackStrategy = "NoAttack";

    private final TroopButton[] availableTroops;

    private boolean collectResources = true;

    private final SettingsPersister configPersister = new SettingsPersister();

    private int darkElixirThreshold;

    private boolean detectEmptyCollectors = true;

    private int elixirThreshold = 100000;

    private boolean extraFunctions;

    private int goldThreshold = 100000;

    private boolean isInitialized;

    private Level logLevel = Level.INFO;

    private boolean matchAllConditions;

    private int maxThThreshold;

    private Class<? extends Platform> preferredPlatform;

    private final TroopButton[] raxInfo = new TroopButton[] { TroopButton.BARB, TroopButton.BARB, TroopButton.ARCHER,
            TroopButton.ARCHER, TroopButton.NO_UNIT, TroopButton.NO_UNIT };

    private int trainMaxTroops = MAX_TRAIN_TROOPS;

    private String userMailAddress = "";

    private UUID uuid = UUID.randomUUID();

    private Settings() {
        availableTroops = new TroopButton[] { TroopButton.NO_UNIT, TroopButton.BARB, TroopButton.ARCHER,
                TroopButton.GIANT, TroopButton.GOBLIN, TroopButton.WB, TroopButton.BALLOON, TroopButton.WIZARD,
                TroopButton.HEALER, TroopButton.DRAGON, TroopButton.PEKKA, TroopButton.MINION, TroopButton.HOGRIDER };
    }

    public String getAttackStrategy() {
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

    public Class<? extends Platform> getPreferredPlatform() {
        return preferredPlatform;
    }

    public TroopButton[] getRaxInfo() {
        return raxInfo;
    }

    public int getTrainMaxTroops() {
        return trainMaxTroops;
    }

    public String getUserMailAddress() {
        return userMailAddress;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isCollectResources() {
        return collectResources;
    }

    public boolean isDetectEmptyCollectors() {
        return detectEmptyCollectors;
    }

    public boolean isExtraFunctions() {
        return extraFunctions;
    }

    public boolean isMatchAllConditions() {
        return matchAllConditions;
    }

    public void save() {
        configPersister.save(this);
    }

    public void setAttackStrategy(final String attackStrategy) {
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

    public void setExtraFunctions(final boolean extraFunctions) {
        this.extraFunctions = extraFunctions;
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

    public void setPreferredPlatform(final Class<? extends Platform> preferredPlatform) {
        this.preferredPlatform = preferredPlatform;
    }

    public void setRaxInfo(final String raxInfoProperty) {
        final int raxCount = raxInfo.length;
        final String[] splits = raxInfoProperty.split("\\s*,\\s*");
        for (int i = 0; i < splits.length && i < raxCount; i++) {
            final String split = splits[i];
            raxInfo[i] = TroopButton.fromDescription(split);
        }
    }

    public void setTrainMaxTroops(final int trainMaxTroops) {
        this.trainMaxTroops = trainMaxTroops;
    }

    public void setUserMailAddress(final String userMailAddress) {
        this.userMailAddress = userMailAddress;
    }

    void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }
}
