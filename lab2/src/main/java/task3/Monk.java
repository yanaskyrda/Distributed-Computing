package task3;

import java.util.Random;

public class Monk {
    private Monastery monastery;
    private int qiEnergy;

    public Monk(Monastery monastery, int qiEnergy) {
        this.monastery = monastery;
        this.qiEnergy = qiEnergy;
    }

    public Monastery getMonastery() {
        return monastery;
    }

    public boolean fight(Monk other) {
        if (this.qiEnergy > other.qiEnergy) {
            return true;
        } else if (other.qiEnergy > this.qiEnergy) {
            return false;
        } else {
            return new Random().nextBoolean();
        }
    }
}
