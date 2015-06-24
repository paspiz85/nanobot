package it.paspiz85.nanobot.attack;

public class ManualAttack extends Attack {

    private static final ManualAttack instance = new ManualAttack();

    public static ManualAttack instance() {
        return instance;
    }

    private ManualAttack() {
    }

    @Override
    protected void doDropUnits(int[] attackGroup) throws InterruptedException {
    }
}
