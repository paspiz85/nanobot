package it.paspiz85.nanobot.attack;

/**
 * Manual attack, no bot actions.
 *
 * @author paspiz85
 *
 */
public final class ManualAttack extends Attack {

    ManualAttack() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
    }
}
