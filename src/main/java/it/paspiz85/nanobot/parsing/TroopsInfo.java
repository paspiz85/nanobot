package it.paspiz85.nanobot.parsing;

/**
 * Troops info.
 *
 * @author paspiz85
 *
 */
public final class TroopsInfo {

    private final int[] troopsCount;

    public TroopsInfo(final int[] troopsCount) {
        this.troopsCount = troopsCount;
    }

    public int[] getTroopsCount() {
        return troopsCount;
    }
}
