package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.attack.Attack2Side;
import it.paspiz85.nanobot.attack.Attack4Side;
import it.paspiz85.nanobot.attack.Attack4SideParallel;
import it.paspiz85.nanobot.attack.Attack4SideParallelFull2Wave;
import it.paspiz85.nanobot.attack.Attack4SideParallelHalf2Wave;
import it.paspiz85.nanobot.attack.ManualAttack;
import it.paspiz85.nanobot.parsing.Clickable;

import java.awt.Point;

public final class Settings {

    private static final Settings instance = new Settings();

    public synchronized static void close() {
        if (!instance.isInitialized) {
            throw new IllegalStateException("ConfigUtils is not initialized.");
        }
        instance.isInitialized = false;
    }

    public synchronized static void initialize() throws IllegalStateException {
        // Throw exception if called twice
        if (instance.isInitialized) {
            throw new IllegalStateException("ConfigUtils is already initialized.");
        }
        instance.configPersister.reload(instance);
        instance.isInitialized = true;
    }

    /**
     * Singleton accessor method.
     */
    public static Settings instance() {
        if (!instance.isInitialized) {
            synchronized (Settings.class) {
                if (!instance.isInitialized) {
                    throw new IllegalStateException("ConfigUtils is not initialized.");
                }
            }
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance.isInitialized;
    }

    private Attack attackStrategy = ManualAttack.instance();

    private final Attack[] availableAttackStrategies;

    private final Clickable[] availableTroops;

    private final SettingsPersister configPersister = new SettingsPersister();

    private int darkElixirThreshold = 0;

    private boolean detectEmptyCollectors = false;

    private int elixirThreshold = 0;

    private Point firstBarrackPosition = null;

    private int goldThreshold = 0;

    private boolean isInitialized = false;

    private boolean logEnemyBase = false;

    private boolean matchAllConditions = false;

    private int maxThThreshold = 0;

    private boolean playSound = false;

    private final Clickable[] raxInfo = new Clickable[] { Clickable.BUTTON_RAX_BARB, Clickable.BUTTON_RAX_BARB,
            Clickable.BUTTON_RAX_ARCHER, Clickable.BUTTON_RAX_ARCHER, };

    private Settings() {
        availableAttackStrategies = new Attack[] { ManualAttack.instance(), Attack2Side.instance(),
                Attack4Side.instance(), Attack4SideParallel.instance(), Attack4SideParallelHalf2Wave.instance(),
                Attack4SideParallelFull2Wave.instance(), };
        availableTroops = new Clickable[] { Clickable.BUTTON_RAX_NO_UNIT, Clickable.BUTTON_RAX_BARB,
                Clickable.BUTTON_RAX_ARCHER, Clickable.BUTTON_RAX_GIANT, Clickable.BUTTON_RAX_GOBLIN,
                Clickable.BUTTON_RAX_WB, Clickable.BUTTON_RAX_BALLOON, Clickable.BUTTON_RAX_WIZARD,
                Clickable.BUTTON_RAX_HEALER, Clickable.BUTTON_RAX_DRAGON, Clickable.BUTTON_RAX_PEKKA };
    }

    public Attack getAttackStrategy() {
        return this.attackStrategy;
    }

    public Attack[] getAvailableAttackStrategies() {
        return availableAttackStrategies;
    }

    public Clickable[] getAvailableTroops() {
        return availableTroops;
    }

    public int getDarkElixirThreshold() {
        return darkElixirThreshold;
    }

    public int getElixirThreshold() {
        return elixirThreshold;
    }

    public Point getFirstBarrackPosition() {
        return firstBarrackPosition;
    }

    public int getGoldThreshold() {
        return goldThreshold;
    }

    public int getMaxThThreshold() {
        return maxThThreshold;
    }

    public Clickable[] getRaxInfo() {
        return raxInfo;
    }

    public boolean isDetectEmptyCollectors() {
        return detectEmptyCollectors;
    }

    public boolean isLogEnemyBase() {
        return logEnemyBase;
    }

    public boolean isMatchAllConditions() {
        return matchAllConditions;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void save() {
        configPersister.save(this);
    }

    private void setAttackStrategy(final Attack attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public void setAttackStrategy(final String attackStrategy) {
        boolean found = false;
        for (final Attack attack : availableAttackStrategies) {
            if (attack.getClass().getSimpleName().equals(attackStrategy)) {
                setAttackStrategy(attack);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException(attackStrategy);
        }
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

    public void setFirstBarrackPosition(final Point firstBarrackPosition) {
        this.firstBarrackPosition = firstBarrackPosition;
    }

    public void setGoldThreshold(final int goldThreshold) {
        this.goldThreshold = goldThreshold;
    }

    public void setLogEnemyBase(final boolean logEnemyBase) {
        this.logEnemyBase = logEnemyBase;
    }

    public void setMatchAllConditions(final boolean matchAllConditions) {
        this.matchAllConditions = matchAllConditions;
    }

    public void setMaxThThreshold(final int maxThThreshold) {
        this.maxThThreshold = maxThThreshold;
    }

    public void setPlaySound(final boolean playSound) {
        this.playSound = playSound;
    }

    public void setRaxInfo(final String raxInfoProperty) {
        final int raxCount = raxInfo.length;
        final String[] splits = raxInfoProperty.split("\\s*,\\s*");
        for (int i = 0; i < splits.length && i < raxCount; i++) {
            final String split = splits[i];
            raxInfo[i] = Clickable.fromDescription(split);
        }
    }
}
