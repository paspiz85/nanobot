package it.paspiz85.nanobot.parsing;

/**
 * Enemy loot.
 *
 * @author paspiz85
 *
 */
public final class Loot {

    private Integer gold;

    private Integer elixir;

    private Integer darkElixir;

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
        final Loot other = (Loot) obj;
        if (darkElixir == null) {
            if (other.darkElixir != null) {
                return false;
            }
        } else if (!darkElixir.equals(other.darkElixir)) {
            return false;
        }
        if (elixir == null) {
            if (other.elixir != null) {
                return false;
            }
        } else if (!elixir.equals(other.elixir)) {
            return false;
        }
        if (gold == null) {
            if (other.gold != null) {
                return false;
            }
        } else if (!gold.equals(other.gold)) {
            return false;
        }
        return true;
    }

    public Integer getDarkElixir() {
        return darkElixir;
    }

    public Integer getElixir() {
        return elixir;
    }

    public Integer getGold() {
        return gold;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (darkElixir == null ? 0 : darkElixir.hashCode());
        result = prime * result + (elixir == null ? 0 : elixir.hashCode());
        result = prime * result + (gold == null ? 0 : gold.hashCode());
        return result;
    }

    public void setDarkElixir(final Integer darkElixir) {
        this.darkElixir = darkElixir;
    }

    public void setElixir(final Integer elixir) {
        this.elixir = elixir;
    }

    public void setGold(final Integer gold) {
        this.gold = gold;
    }
}
