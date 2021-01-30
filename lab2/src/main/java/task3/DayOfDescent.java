package task3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class DayOfDescent {
    private List<Monk> fighterMonks;

    public DayOfDescent(List<Integer> guanYinEnergys, List<Integer> guanYangEnergys) {
        this.fighterMonks = new ArrayList<>(guanYinEnergys.size() + guanYangEnergys.size());
        initializeMonks(guanYinEnergys, Monastery.GUAN_YIN);
        initializeMonks(guanYangEnergys, Monastery.GUAN_YANG);
    }

    private void initializeMonks(List<Integer> energys, Monastery monastery) {
        for (Integer energy : energys) {
            fighterMonks.add(new Monk(monastery, energy));
        }
    }

    public Monastery celebrate() {
        Monk winner = ForkJoinPool.commonPool().
                invoke(new FistPathCompetition(fighterMonks, 0, fighterMonks.size()));
        return winner.getMonastery();
    }

    public static void main(String... args) {
        DayOfDescent bigHoliday = new DayOfDescent(
                Arrays.asList(12, 4, 15, 32, 16, 13, 18, 52),
                Arrays.asList(4, 16, 34, 27, 30, 10, 13, 52, 18)
        );

        System.out.println("Winner of the competition: " + bigHoliday.celebrate());
    }
}
