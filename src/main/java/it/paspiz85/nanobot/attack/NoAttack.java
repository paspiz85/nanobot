package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.os.OS;

/**
 * No attack, no bot actions.
 *
 * @author paspiz85
 *
 */
public final class NoAttack extends Attack {

    NoAttack(final OS os) {
        super(os);
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
    }
}
