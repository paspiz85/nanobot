package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.win32.OS;

public class StateTrainTroops extends State {

    private static StateTrainTroops instance = new StateTrainTroops();

    public static StateTrainTroops instance() {
        return instance;
    }

    private StateTrainTroops() {
    }

    @Override
    public void handle(Context context) throws InterruptedException {
        logger.info("Training Troops");
        // first barracks must be opened at this point
        Clickable[] raxInfo = Settings.instance().getRaxInfo();
        for (int currRax = 0; currRax < raxInfo.length; currRax++) {
            Clickable troop = raxInfo[currRax];
            if (troop != Clickable.BUTTON_RAX_NO_UNIT) {
                for (int i = 0; i < OS.random().nextInt(5) + 15; i++) {
                    OS.instance().leftClick(troop, 75);
                }
            }
            if (currRax < raxInfo.length - 1) {
                // select next rax
                OS.instance().leftClick(Clickable.BUTTON_RAX_NEXT, 350);
            }
        }
        OS.instance().leftClick(Clickable.BUTTON_RAX_CLOSE, 250);
        context.setState(StateMainMenu.instance());
        OS.instance().sleepRandom(5000);
    }
}
