package it.paspiz85.nanobot.attack;

public final class ManualAttack extends Attack {

    private static ManualAttack instance;

    public static ManualAttack instance() {
        if (instance == null) {
            instance = new ManualAttack();
        }
        return instance;
    }

    private ManualAttack() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
    }
}
