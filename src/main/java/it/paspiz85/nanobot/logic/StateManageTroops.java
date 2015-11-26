package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.game.ManageTroopsScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.game.Troop;
import it.paspiz85.nanobot.game.TroopsInfo;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.logging.Level;

/**
 * This state is when training troops.
 *
 * @author paspiz85
 *
 */
public final class StateManageTroops extends State<ManageTroopsScreen> {

    public static StateManageTroops instance() {
        return Utils.singleton(StateManageTroops.class, () -> new StateManageTroops());
    }

    private StateManageTroops() {
        super(Screen.getInstance(ManageTroopsScreen.class));
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        logger.log(Level.FINE, "Managing troops");
        if (Thread.interrupted()) {
            throw new InterruptedException(getClass().getSimpleName() + " is interrupted.");
        }
        final TroopsInfo troopsInfo = getScreen().parseTroopsInfo();
        context.setTroopsInfo(troopsInfo);
        logger.log(Level.CONFIG, "Troops count: " + troopsInfo);
        logger.log(Level.FINE, "Open first barrack");
        platform.leftClick(getScreen().getButtonTrainNext(), true);
        platform.sleepRandom(500);
        final int trainMaxTroops = Settings.instance().getTrainMaxTroops();
        logger.log(Level.FINE, "Train Max Troops is " + trainMaxTroops);
        int troopsCountSum = troopsInfo.getTroopsCountSum();
        if ((getScreen().areCampsFull() || troopsCountSum >= trainMaxTroops)
                && !Attack.noStrategy().equals(Settings.instance().getAttackStrategy())) {
            logger.log(Level.INFO, "Camp is full");
            logger.log(Level.FINE, "Close barracks");
            platform.leftClick(getScreen().getButtonTrainClose(), true);
            platform.sleepRandom(200);
            StateMainMenu.instance().handleAttack(context);
        } else {
            if (trainMaxTroops > 0) {
                logger.log(Level.CONFIG, "Training Troops");
                final Troop[] raxInfo = Settings.instance().getRaxInfo();
                for (int currRax = 0; currRax < raxInfo.length; currRax++) {
                    final Troop troop = raxInfo[currRax];
                    if (troop != Troop.NO_UNIT) {
                        final int clicks = 10 + Utils.RANDOM.nextInt(10);
                        logger.log(Level.FINE, "Try training " + clicks + " " + troop.getDescription());
                        for (int i = 0; i < clicks; i++) {
                            platform.leftClick(troop.getTrainButton(), true);
                            platform.sleepRandom(75);
                        }
                    }
                    if (currRax < raxInfo.length - 1) {
                        logger.log(Level.FINE, "Goto next barrack");
                        platform.leftClick(getScreen().getButtonTrainNext(), true);
                        platform.sleepRandom(350);
                    }
                }
            }
            StateManageTroopsEnd.instance().handle(context);
            // waiting minimum time
            platform.sleepRandom(Math.max(5000, 40000 - 5000 * context.getTrainCount()));
        }
        platform.sleepRandom(500);
    }
}
