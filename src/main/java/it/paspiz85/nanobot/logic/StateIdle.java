package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.game.AttackScreen;
import it.paspiz85.nanobot.game.BattleBeginScreen;
import it.paspiz85.nanobot.game.BattleEndScreen;
import it.paspiz85.nanobot.game.MainScreen;
import it.paspiz85.nanobot.game.ManageTroopsScreen;
import it.paspiz85.nanobot.game.PlatformScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

import java.util.logging.Level;

/**
 * This state is when bot sleeps.
 *
 * @author paspiz85
 *
 */
public final class StateIdle extends State<Screen> {

    public static StateIdle instance() {
        return Utils.singleton(StateIdle.class, () -> new StateIdle());
    }

    private final MainScreen mainScreenParser;

    private Looper looper;

    private StateIdle() {
        super(Screen.getInstance(null));
        mainScreenParser = Screen.getInstance(MainScreen.class);
    }

    @Override
    public void handle(final Context context) throws InterruptedException {
        State<?> nextState = this;
        logger.log(Level.INFO, "Idle");
        while (looper.isRunning()) {
            if (Thread.interrupted()) {
                throw new InterruptedException(getClass().getSimpleName() + " is interrupted");
            }
            if (looper.isReloading()) {
                logger.log(Level.INFO, "reloading...");
                platform.zoomUp();
                Thread.sleep(2000);
                continue;
            } else {
                platform.sleepRandom(350);
            }
            if (platform.matchColoredPoint(mainScreenParser.getPointWasAttackedHeadline())
                    || platform.matchColoredPoint(mainScreenParser.getButtonWasAttackedOK())) {
                logger.log(Level.INFO, "Was attacked");
                platform.leftClick(mainScreenParser.getButtonWasAttackedOK(), true);
                platform.sleepRandom(250);
            } else if (Screen.getInstance(BattleEndScreen.class).isDisplayed()) {
                logger.log(Level.INFO, "Detected BattleEndScreen");
                nextState = StateBattleEnd.instance();
                break;
            } else if (Screen.getInstance(ManageTroopsScreen.class).isDisplayed()) {
                logger.log(Level.INFO, "Detected ManageTroopsScreen");
                nextState = StateManageTroopsEnd.instance();
                break;
            } else if (Screen.getInstance(MainScreen.class).isDisplayed()) {
                logger.log(Level.INFO, "Detected MainScreen");
                nextState = StateMainMenu.instance();
                break;
            } else if (Screen.getInstance(AttackScreen.class).isDisplayed()) {
                logger.log(Level.INFO, "Detected AttackScreen");
                nextState = StateAttack.instance();
                break;
            } else if (Screen.getInstance(BattleBeginScreen.class).isDisplayed()) {
                logger.log(Level.INFO, "Detected BattleBeginScreen");
                nextState = StateBattleBegin.instance();
                break;
            } else if (Screen.getInstance(PlatformScreen.class).isDisplayed()) {
                logger.log(Level.INFO, "Detected PlatformScreen");
                final Point point = Screen.getInstance(PlatformScreen.class).searchButtonPlayGame();
                if (point != null) {
                    platform.leftClick(point, true);
                }
            }
            Thread.sleep(1000);
        }
        context.setState(nextState);
    }

    public void setLooper(final Looper looper) {
        this.looper = looper;
    }
}
