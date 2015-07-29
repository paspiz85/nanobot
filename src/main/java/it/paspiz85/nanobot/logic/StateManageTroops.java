package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.parsing.TroopButton;
import it.paspiz85.nanobot.parsing.TroopsInfo;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.Arrays;
import java.util.logging.Level;

/**
 * This state is when training troops.
 *
 * @author paspiz85
 *
 */
public final class StateManageTroops extends State<MainScreenParser> {

    public static StateManageTroops instance() {
        return Utils.singleton(StateManageTroops.class, () -> new StateManageTroops());
    }

    private boolean reloading;

    private StateManageTroops() {
        super(Parser.getInstance(MainScreenParser.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.log(Level.FINE, "Managing troops");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted.");
        }
        final TroopsInfo troopsInfo = getParser().parseTroopsInfo();
        final int[] troopsCount = troopsInfo.getTroopsCount();
        logger.log(Level.CONFIG, "Troops count: " + Arrays.toString(troopsCount));
        context.setTroopsInfo(troopsInfo);
        logger.log(Level.FINE, "Open first barrack");
        platform.leftClick(getParser().getButtonTrainNext(), true);
        platform.sleepRandom(500);
        int troopsCountSum = 0;
        for (final int i : troopsCount) {
            troopsCountSum += i;
        }
        final int trainMaxTroops = Settings.instance().getTrainMaxTroops();
        if (getParser().areCampsFull() || troopsCountSum >= trainMaxTroops) {
            logger.log(Level.INFO, "Camp is full");
            logger.log(Level.FINE, "Close barracks");
            platform.leftClick(getParser().getButtonTrainClose(), true);
            platform.sleepRandom(200);
            logger.log(Level.FINE, "Press Attack");
            platform.leftClick(getParser().getButtonAttack(), true);
            platform.sleepRandom(1000);
            context.setState(StateFindAMatch.instance());
        } else {
            if (trainMaxTroops > 0) {
                logger.log(Level.INFO, "Training Troops");
                final TroopButton[] raxInfo = Settings.instance().getRaxInfo();
                for (int currRax = 0; currRax < raxInfo.length; currRax++) {
                    final TroopButton troop = raxInfo[currRax];
                    if (troop != TroopButton.NO_UNIT) {
                        final int clicks = 10 + Utils.RANDOM.nextInt(10);
                        logger.log(Level.FINE, "Try training " + clicks + " " + troop.getDescription());
                        for (int i = 0; i < clicks; i++) {
                            platform.leftClick(troop.getPoint(), true);
                            platform.sleepRandom(75);
                        }
                    }
                    if (currRax < raxInfo.length - 1) {
                        logger.log(Level.FINE, "Goto next barrack");
                        platform.leftClick(getParser().getButtonTrainNext(), true);
                        platform.sleepRandom(350);
                    }
                }
            }
            logger.log(Level.FINE, "Close Training Troops");
            platform.leftClick(getParser().getButtonTrainClose(), true);
            platform.sleepRandom(250);
            context.setState(StateMainMenu.instance());
            // waiting minimum time
            platform.sleepRandom(Math.max(5000, 40000 - 5000 * context.getTrainCount()));
        }
        platform.sleepRandom(500);
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(final boolean reloading) {
        this.reloading = reloading;
    }
}
