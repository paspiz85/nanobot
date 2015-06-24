package it.paspiz85.nanobot.state;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Robot;
import it.paspiz85.nanobot.util.Settings;

public class StateTrainTroops extends State {

    private static StateTrainTroops instance = new StateTrainTroops();

    public static StateTrainTroops instance() {
        return instance;
    }

    private StateTrainTroops() {
    }

    @Override
    public void handle(Context context) throws InterruptedException {
        logger.info("StateTrainTroops");
        // first barracks must be opened at this point
        Clickable[] raxInfo = Settings.instance().getRaxInfo();
        for (int currRax = 0; currRax < raxInfo.length; currRax++) {
            Clickable troop = raxInfo[currRax];
            if (troop != Clickable.BUTTON_RAX_NO_UNIT) {
                for (int i = 0; i < Robot.random().nextInt(5) + 15; i++) {
                    Robot.instance().leftClick(troop, 75);
                }
            }
            if (currRax < raxInfo.length - 1) {
                // select next rax
                Robot.instance().leftClick(Clickable.BUTTON_RAX_NEXT, 350);
            }
        }
        Robot.instance().leftClick(Clickable.BUTTON_RAX_CLOSE, 250);
        context.setState(StateMainMenu.instance());
        Robot.instance().sleepRandom(5000);
    }
}
