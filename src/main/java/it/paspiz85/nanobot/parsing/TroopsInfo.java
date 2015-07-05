package it.paspiz85.nanobot.parsing;

/**
 * Troops info.
 *
 * @author paspiz85
 *
 */
public final class TroopsInfo {
    
    private int[] troopsCount;

    public TroopsInfo(int[] troopsCount) {
        this.troopsCount = troopsCount;
    }

    public int[] getTroopsCount() {
        return troopsCount;
    }
}
