package it.paspiz85.nanobot.attack;

import it.paspiz85.nanobot.game.TroopsInfo;

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
    protected void doDropUnits(final TroopsInfo troopsInfo) throws InterruptedException {
    }

    @Override
    protected String getDescription() {
        return "Manually drop units";
    }
}
