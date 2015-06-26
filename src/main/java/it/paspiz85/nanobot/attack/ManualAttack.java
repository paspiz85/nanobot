package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.logic.OS;

/**
 * Manual attack, no bot actions.
 *
 * @author paspiz85
 *
 */
public final class ManualAttack extends Attack {

    ManualAttack(final OS os) {
        super(os);
    }

    @Override
    protected void doDropUnits(final int[] attackGroup) throws InterruptedException {
    }
}
