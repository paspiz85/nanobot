package it.paspiz85.nanobot.parsing;

public final class Parsers {

    private static final AttackScreenParser attackScreen = new AttackScreenParser();

    private static final MainScreenParser mainScreen = new MainScreenParser();

    public static AttackScreenParser getAttackScreen() {
        return attackScreen;
    }

    public static MainScreenParser getMainscreen() {
        return mainScreen;
    }
}
