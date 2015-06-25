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
        // first barracks must be opened at this point
        logger.info("Training Troops");
        final Clickable[] raxInfo = Settings.instance().getRaxInfo();
        for (int currRax = 0; currRax < raxInfo.length; currRax++) {
            final Clickable troop = raxInfo[currRax];
            if (troop != Clickable.BUTTON_RAX_NO_UNIT) {
                final int clicks = BARRACK_LV10_SIZE / 2 + OS.random().nextInt(BARRACK_LV10_SIZE);
                logger.fine("Try training " + clicks + " " + troop.getDescription());
                for (int i = 0; i < clicks; i++) {
                    OS.instance().leftClick(troop, true);
                    OS.instance().sleepRandom(75);
                }
            }
            if (currRax < raxInfo.length - 1) {
                logger.fine("Goto next barrack");
                OS.instance().leftClick(Clickable.BUTTON_RAX_NEXT, true);
                OS.instance().sleepRandom(350);
            }
        }
        logger.fine("Close Training Troops");
        OS.instance().leftClick(Clickable.BUTTON_RAX_CLOSE, true);
        OS.instance().sleepRandom(250);
        context.setState(StateMainMenu.instance());
        // waiting minimum time
        OS.instance().sleepRandom(Math.max(5000, BARRACK_LV1_SIZE * BARB_TRAIN_MS / (4 * context.getTrainCount())));
    }
}
