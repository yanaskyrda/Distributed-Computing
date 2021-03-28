package task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SoldiersGroup implements Runnable {

    private static final ArrayList<Boolean> partFinished = new ArrayList<>();
    private static final AtomicBoolean finished = new AtomicBoolean(false);

    static {
        for (int i = 0; i < Application.partsNumber; i++) {
            partFinished.add(false);
        }
    }

    private final Turn[] soldier;
    private final int groupNum;
    private final int indexOfLeft;
    private final int indexOfRight;
    private final CustomCyclicBarrier cyclicBarrier;

    SoldiersGroup(Turn[] soldier, int groupNum, int indexOfLeft, int indexOfRight,
                  CustomCyclicBarrier cyclicBarrier) {
        this.soldier = soldier;
        this.groupNum = groupNum;
        this.indexOfLeft = indexOfLeft;
        this.indexOfRight = indexOfRight;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        while (!finished.get()) {
            boolean currFinished = partFinished.get(groupNum);
            if (!currFinished) {
                System.out.println(Arrays.toString(soldier));
                boolean turnedCorrectly = true;
                for (int i = indexOfLeft; i < indexOfRight - 1; i++) {
                    if (soldier[i] != soldier[i + 1]) {
                        soldier[i] = Turn.values()[(soldier[i].ordinal() + 1) % 2];
                        turnedCorrectly = false;
                    }
                }
                if (turnedCorrectly) {
                    checkIfFinished();
                }
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void checkIfFinished() {
        partFinished.set(groupNum, true);
        for (boolean currentPartFinished : partFinished) {
            if (!currentPartFinished) {
                return;
            }
        }
        finished.set(true);
    }
}
