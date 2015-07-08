package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Settings;

/**
 * Training state is when bot train troops.
 *
 * @author paspiz85
 *
 */
public final class StateTrainTroops extends State<MainScreenParser> {

    private static StateTrainTroops instance;

    public static StateTrainTroops instance() {
        if (instance == null) {
            instance = new StateTrainTroops();
        }
        return instance;
    }

    private StateTrainTroops() {
        super(Parser.getInstance(MainScreenParser.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        // first barracks must be opened at this point
        logger.info("Training Troops.");
        final Clickable[] raxInfo = Settings.instance().getRaxInfo();
        for (int currRax = 0; currRax < raxInfo.length; currRax++) {
            final Clickable troop = raxInfo[currRax];
            if (troop != Clickable.BUTTON_RAX_NO_UNIT) {
                final int clicks = 10 + OS.RANDOM.nextInt(10);
                logger.fine("Try training " + clicks + " " + troop.getDescription());
                for (int i = 0; i < clicks; i++) {
                    os.leftClick(troop.getPoint(), true);
                    os.sleepRandom(75);
                }
            }
            if (currRax < raxInfo.length - 1) {
                logger.fine("Goto next barrack");
                os.leftClick(getParser().getButtonTrainNext(), true);
                os.sleepRandom(350);
            }
        }
        logger.fine("Close Training Troops.");
        os.leftClick(getParser().getButtonTrainClose(), true);
        os.sleepRandom(250);
        context.setState(StateMainMenu.instance());
        // waiting minimum time
        os.sleepRandom(Math.max(5000, 40000 - 5000 * context.getTrainCount()));
    }
}
