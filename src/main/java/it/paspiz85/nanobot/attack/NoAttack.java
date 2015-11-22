package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.game.TroopsInfo;

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
    protected void doDropUnits(final TroopsInfo troopsInfo) throws InterruptedException {
    }

    @Override
    protected String getDescription() {
        return "No drop units";
    }
}
