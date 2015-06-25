package it.paspiz85.nanobot.util;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.attack.Attack2Side;
import it.paspiz85.nanobot.attack.Attack4Side;
import it.paspiz85.nanobot.attack.Attack4SideParallel;
import it.paspiz85.nanobot.attack.Attack4SideParallelFull2Wave;
import it.paspiz85.nanobot.attack.Attack4SideParallelHalf2Wave;
import it.paspiz85.nanobot.attack.ManualAttack;
import it.paspiz85.nanobot.parsing.Clickable;

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
    // TODO refactore singleton
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

    private Attack attackStrategy = ManualAttack.instance();

    private final Attack[] availableAttackStrategies;

    private final Clickable[] availableTroops;

    private final SettingsPersister configPersister = new SettingsPersister();

    private int darkElixirThreshold;

    private boolean detectEmptyCollectors;

    private int elixirThreshold;

    private Point firstBarrackPosition;

    private Level logLevel = Level.INFO;

    private int goldThreshold;

    private boolean isInitialized;

    private boolean logEnemyBase;

    private boolean matchAllConditions;

    private int maxThThreshold;

    private boolean playSound;

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

    public Level getLogLevel() {
        return logLevel;
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

    public void setAttackStrategy(final Attack attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    void setAttackStrategy(final String attackStrategy) {
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

    public void setLogLevel(final Level logLevel) {
        this.logLevel = logLevel;
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
