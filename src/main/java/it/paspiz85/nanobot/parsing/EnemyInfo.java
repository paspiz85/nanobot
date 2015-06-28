package it.paspiz85.nanobot.parsing;

/**
 * Enemy info.
 *
 * @author paspiz85
 *
 */
public final class EnemyInfo {

    private Integer gold;

    private Integer elixir;

    private Integer darkElixir;

    private Integer trophyWin;

    private Integer trophyDefeat;

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
        final EnemyInfo other = (EnemyInfo) obj;
        if (darkElixir == null) {
            if (other.darkElixir != null) {
                return false;
            }
        } else if (!darkElixir.equals(other.darkElixir)) {
            return false;
        }
        if (trophyDefeat == null) {
            if (other.trophyDefeat != null) {
                return false;
            }
        } else if (!trophyDefeat.equals(other.trophyDefeat)) {
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
        if (trophyWin == null) {
            if (other.trophyWin != null) {
                return false;
            }
        } else if (!trophyWin.equals(other.trophyWin)) {
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

    public Integer getTrophyDefeat() {
        return trophyDefeat;
    }

    public Integer getTrophyWin() {
        return trophyWin;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (darkElixir == null ? 0 : darkElixir.hashCode());
        result = prime * result + (trophyDefeat == null ? 0 : trophyDefeat.hashCode());
        result = prime * result + (elixir == null ? 0 : elixir.hashCode());
        result = prime * result + (gold == null ? 0 : gold.hashCode());
        result = prime * result + (trophyWin == null ? 0 : trophyWin.hashCode());
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

    public void setTrophyDefeat(final Integer trophyDefeat) {
        this.trophyDefeat = trophyDefeat;
    }

    public void setTrophyWin(final Integer trophyWin) {
        this.trophyWin = trophyWin;
    }

    @Override
    public String toString() {
        return String.format("[gold: %d, elixir: %d, de: %d]", gold, elixir, darkElixir);
    }
}
