package it.paspiz85.nanobot.game;

import java.util.List;
import java.util.Map.Entry;

/**
 * Troops info.
 *
 * @author paspiz85
 *
 */
public final class TroopsInfo {

    private final List<Entry<Troop, Integer>> troopsCount;

    public TroopsInfo(final List<Entry<Troop, Integer>> troopsCount) {
        this.troopsCount = troopsCount;
    }

    public int[] getTroopsCount() {
        return troopsCount.stream().mapToInt((e) -> e.getValue()).toArray();
    }

    // TODO weighted sum
    public int getTroopsCountSum() {
        return troopsCount.stream().mapToInt((e) -> e.getValue()).sum();
    }
}
