package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.win32.OS;

/**
 * Training state is when bot train troops.
 *
 * @author v-ppizzuti
 *
 */
public final class StateTrainTroops extends State {

    private static StateTrainTroops instance;

    private static final int BARRACK_LV1_SIZE = 20;

    private static final int BARB_TRAIN_MS = 20000;

    public static StateTrainTroops instance() {
        if (instance == null) {
            instance = new StateTrainTroops();
        }
        return instance;
    }

    private StateTrainTroops() {
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.info("Training Troops");
        // first barracks must be opened at this point
        final Clickable[] raxInfo = Settings.instance().getRaxInfo();
        for (int currRax = 0; currRax < raxInfo.length; currRax++) {
            final Clickable troop = raxInfo[currRax];
            if (troop != Clickable.BUTTON_RAX_NO_UNIT) {
                for (int i = 0; i < OS.random().nextInt(5) + 15; i++) {
                    OS.instance().leftClick(troop.getPoint(), true);
                    OS.instance().sleepRandom(75);
                }
            }
            if (currRax < raxInfo.length - 1) {
                // select next rax
                OS.instance().leftClick(Clickable.BUTTON_RAX_NEXT.getPoint(), true);
                OS.instance().sleepRandom(350);
            }
        }
        OS.instance().leftClick(Clickable.BUTTON_RAX_CLOSE.getPoint(), true);
        OS.instance().sleepRandom(250);
        context.setState(StateMainMenu.instance());
        OS.instance().sleepRandom(BARRACK_LV1_SIZE * BARB_TRAIN_MS / 2);
    }
}
