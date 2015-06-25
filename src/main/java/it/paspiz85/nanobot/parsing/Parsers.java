package it.paspiz85.nanobot.parsing;

/**
 * Factory of parsers.
 *
 * @author v-ppizzuti
 *
 */
public final class Parsers {

    private static AttackScreenParser attackScreen;

    private static MainScreenParser mainScreen;

    public static AttackScreenParser getAttackScreen() {
        if (attackScreen == null) {
            attackScreen = new AttackScreenParser();
        }
        return attackScreen;
    }

    public static MainScreenParser getMainscreen() {
        if (mainScreen == null) {
            mainScreen = new MainScreenParser();
        }
        return mainScreen;
    }

    private Parsers() {
    }
}
