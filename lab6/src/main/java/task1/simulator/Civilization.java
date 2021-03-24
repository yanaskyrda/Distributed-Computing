package task1.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Civilization implements Runnable {
    private CyclicBarrier barrier;
    private Simulation simulation;
    private int start;
    private int taskSize;

    Civilization(CyclicBarrier barrier, Simulation simulation, int start, int taskSize) {
        this.barrier = barrier;
        this.simulation = simulation;
        this.start = start;
        this.taskSize = taskSize;
    }

    private boolean insideField(int i, int j, List<List<Integer>> field) {
        return i >= 0 && i < field.size() && j >= 0 && j < field.get(0).size();
    }

    private int countNeighbors(int i, int j, List<List<Integer>> field) {
        int result = 0;
        List<Integer> shifts = Arrays.asList(-1, 0, 1);
        for (int shiftByX : shifts) {
            for (int shiftByY : shifts) {
                if (shiftByX == 0 && shiftByY == 0) {
                    continue;
                }

                int x = i + shiftByX;
                int y = j + shiftByY;
                if (insideField(x, y, field)) {
                    if (field.get(x).get(y) == 1) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void run() {
        while (true) {
            while (!Manager.updated) {
                Thread.yield();
            }
            List<List<Integer>> map = simulation.getField();
            List<List<Integer>> newMap = new ArrayList<>();

            for (int i = 0; i < taskSize; i++) {
                List<Integer> row = new ArrayList<>();
                for (int j = 0; j < Manager.MAP_SIZE; j++) {
                    row.add(0);
                }
                newMap.add(row);
            }

            for (int i = start; i < start + taskSize; i++) {
                for (int j = 0; j < Manager.MAP_SIZE; j++) {
                    newMap.get(i - start).set(j, map.get(i).get(j));
                    int neighborsAmount = countNeighbors(i, j, map);

                    if (map.get(i).get(j) == 0) {
                        if (neighborsAmount == 3) {
                            newMap.get(i - start).set(j, 1);
                        }
                    } else {
                        if (neighborsAmount < 2 || neighborsAmount > 3) {
                            newMap.get(i - start).set(j, 0);
                        }
                    }
                }
            }
            for (int i = start; i < start + taskSize; ++i) {
                for (int j = 0; j < Manager.MAP_SIZE; ++j) {
                    map.get(i).set(j, newMap.get(i - start).get(j));
                }
            }
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
