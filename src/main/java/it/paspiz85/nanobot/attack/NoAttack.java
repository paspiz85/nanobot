package it.paspiz85.nanobot.attack;

/**
 * No attack, no bot actions.
 *
 * @author paspiz85
 *
 */
public final class NoAttack extends Attack {

    NoAttack() {
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
    }
}
