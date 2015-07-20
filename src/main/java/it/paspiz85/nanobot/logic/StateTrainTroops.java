package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.parsing.TroopButton;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

/**
 * Training state is when bot train troops.
 *
 * @author paspiz85
 *
 */
public final class StateTrainTroops extends State<MainScreenParser> {

    public static StateTrainTroops instance() {
        return Utils.singleton(StateTrainTroops.class, () -> new StateTrainTroops());
    }

    private StateTrainTroops() {
        super(Parser.getInstance(MainScreenParser.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        if (Settings.instance().isTrainTroops()) {
            logger.info("Training Troops.");
            final TroopButton[] raxInfo = Settings.instance().getRaxInfo();
            for (int currRax = 0; currRax < raxInfo.length; currRax++) {
                final TroopButton troop = raxInfo[currRax];
                if (troop != TroopButton.NO_UNIT) {
                    final int clicks = 10 + Utils.RANDOM.nextInt(10);
                    logger.fine("Try training " + clicks + " " + troop.getDescription());
                    for (int i = 0; i < clicks; i++) {
                        platform.leftClick(troop.getPoint(), true);
                        platform.sleepRandom(75);
                    }
                }
                if (currRax < raxInfo.length - 1) {
                    logger.fine("Goto next barrack");
                    platform.leftClick(getParser().getButtonTrainNext(), true);
                    platform.sleepRandom(350);
                }
            }
        }
        logger.fine("Close Training Troops.");
        platform.leftClick(getParser().getButtonTrainClose(), true);
        platform.sleepRandom(250);
        context.setState(StateMainMenu.instance());
        // waiting minimum time
        platform.sleepRandom(Math.max(5000, 40000 - 5000 * context.getTrainCount()));
    }
}
