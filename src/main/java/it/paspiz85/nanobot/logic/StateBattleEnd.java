package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.game.BattleEndScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Utils;

import java.util.logging.Level;

/**
 * End-battle state is when bot finished attack.
 *
 * @author paspiz85
 *
 */
public final class StateBattleEnd extends State<BattleEndScreen> {

    public static StateBattleEnd instance() {
        return Utils.singleton(StateBattleEnd.class, () -> new StateBattleEnd());
    }

    private StateBattleEnd() {
        super(Screen.getInstance(BattleEndScreen.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.log(Level.FINE, "Battle end");
        platform.leftClick(getScreen().getButtonReturnHome(), true);
        platform.sleepRandom(3000);
        context.setState(StateIdle.instance());
    }
}
