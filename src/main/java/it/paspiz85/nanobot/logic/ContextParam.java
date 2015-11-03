package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.game.TroopsInfo;

/**
 * Parameters for {@link Context}.
 *
 * @author paspiz85
 *
 */
public enum ContextParam {
    TRAIN_COUNT(Integer.class), TROOPS_INFO(TroopsInfo.class);

    private final Class<?> type;

    private ContextParam(Class<?> type) {
        this.type = type;
    }

    public void check(Object value) {
        if (type.isInstance(value)) {
            throw new IllegalArgumentException("value '" + value + "' is not of type '" + type + "'");
        }
    }
}
