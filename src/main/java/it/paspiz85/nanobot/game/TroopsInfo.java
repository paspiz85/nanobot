package it.paspiz85.nanobot.game;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Troops info.
 *
 * @author paspiz85
 *
 */
public final class TroopsInfo {

    private static final String TROOP_DELIMITER = ":";

    private static final String TO_STRING_DELIMITER = ",";

    public static TroopsInfo parse(final String str) {
        final String content = str.substring(1, str.length() - 1);
        final TroopsInfo troopsInfo = new TroopsInfo();
        if (!content.isEmpty()) {
            final String[] split = content.split(TO_STRING_DELIMITER);
            for (final String element : split) {
                final String[] ts = element.split(TROOP_DELIMITER);
                if (ts.length != 2) {
                    throw new IllegalArgumentException("Unparseable");
                }
                troopsInfo.add(Troop.fromDescription(ts[1]), Integer.valueOf(ts[0]));
            }
        }
        return troopsInfo;
    }

    private final List<Entry<Troop, Integer>> troopsCount;

    public TroopsInfo() {
        this.troopsCount = new ArrayList<>();
    }

    public void add(final Troop troop, final int n) {
        troopsCount.add(new SimpleEntry<Troop, Integer>(troop, n));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TroopsInfo other = (TroopsInfo) obj;
        if (troopsCount == null) {
            if (other.troopsCount != null) {
                return false;
            }
        } else if (!troopsCount.equals(other.troopsCount)) {
            return false;
        }
        return true;
    }

    public int[] getTroopsCount() {
        return troopsCount.stream().mapToInt((e) -> e.getValue()).toArray();
    }

    public int getTroopsCountSum() {
        return troopsCount.stream().mapToInt((e) -> e.getValue() * e.getKey().getPosition()).sum();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (troopsCount == null ? 0 : troopsCount.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "["
                + troopsCount.stream().map((e) -> e.getValue() + TROOP_DELIMITER + e.getKey().getDescription())
                        .collect(Collectors.joining(TO_STRING_DELIMITER)) + "]";
    }
}
