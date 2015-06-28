package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.util.Point;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;


public class Model {
    
    //TODO remove singleton def and usage
    private Looper looper = Looper.instance();

    private static Model instance;

    public static Model instance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    
    private Model() {
        
    }

    public boolean isRunning() {
        return looper.isRunning();
    }

    public void start(BooleanSupplier setupResolution, Supplier<Point> setupBarracks) throws InterruptedException,
            BotException {
        looper.start(setupResolution, setupBarracks);
    }
}
