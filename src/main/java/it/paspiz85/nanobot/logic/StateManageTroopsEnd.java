package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.game.ManageTroopsScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Utils;

import java.util.logging.Level;

/**
 * This state is when training troops.
 *
 * @author paspiz85
 *
 */
public final class StateManageTroopsEnd extends State<ManageTroopsScreen> {

    public static StateManageTroopsEnd instance() {
        return Utils.singleton(StateManageTroopsEnd.class, () -> new StateManageTroopsEnd());
    }

    private StateManageTroopsEnd() {
        super(Screen.getInstance(ManageTroopsScreen.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.log(Level.FINE, "Training end");
        platform.leftClick(getScreen().getButtonTrainClose(), true);
        platform.sleepRandom(250);
        context.setState(StateMainMenu.instance());
    }
}
