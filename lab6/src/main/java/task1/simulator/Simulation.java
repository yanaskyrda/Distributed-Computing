package task1.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Simulation implements Runnable {
    private List<List<Integer>> field;
    private CellsMap cellsMap;
    private CyclicBarrier barrier;

    List<List<Integer>> getField() {
        return field;
    }

    Simulation(CellsMap cellsMap, CyclicBarrier barrier, int row_number, int column_number) {
        this.cellsMap = cellsMap;
        this.barrier = barrier;
        field = new ArrayList<>();
        for (int i = 0; i < row_number; ++i) {
            List<Integer> row = new ArrayList<>();
            Random random = new Random();
            for (int j = 0; j < column_number; ++j) {
                row.add(Math.abs(random.nextInt() % (Manager.CIVILIZATION_NUMBER + 1)));
            }
            field.add(row);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            cellsMap.putInSecondary(field);
            Manager.updated = true;
        }
    }
}
