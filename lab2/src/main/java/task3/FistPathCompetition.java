package task3;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FistPathCompetition extends RecursiveTask<Monk> {
    private List<Monk> fighterMonks;
    private int start;
    private int end;

    FistPathCompetition(List<Monk> fighterMonks, int start, int end) {
        this.fighterMonks = fighterMonks;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Monk compute() {
        if (end - start < 3) {
            Monk leftFighter = fighterMonks.get(start);
            Monk rightFighter = fighterMonks.get(end - 1);
            return leftFighter.fight(rightFighter) ? leftFighter : rightFighter;
        } else {
            int middle = (start + end) / 2;
            FistPathCompetition leftFight = new FistPathCompetition(fighterMonks, start, middle);
            FistPathCompetition rightFight = new FistPathCompetition(fighterMonks, middle, end);

            leftFight.fork();
            rightFight.fork();

            Monk leftFighter = leftFight.join();
            Monk rightFighter = rightFight.join();
            return leftFighter.fight(rightFighter) ? leftFighter : rightFighter;
        }
    }
}
