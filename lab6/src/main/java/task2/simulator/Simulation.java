package task2.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {
    private List<List<Integer>> map;
    private int rowsAmount;
    private int columnsAmount;
    private CellsMap cellsMap;
    
    Simulation(CellsMap cellsMap, int rowsAmount, int columnsAmount) {
        this.cellsMap = cellsMap;
        this.rowsAmount = rowsAmount;
        this.columnsAmount = columnsAmount;
        map = new ArrayList<>();

        for (int i = 0; i < rowsAmount; ++i) {
            Random random = new Random();
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < columnsAmount; ++j) {
                row.add(Math.abs(random.nextInt() % Manager.CIVILIZATION_NUMBER + 1));
            }
            map.add(row);
        }
    }

    private boolean insideField(int i, int j) {
        return i >= 0 && i < map.size() && j >= 0 && j < map.get(0).size();
    }

    private int countFriendlyNeighbors(int i, int j) {
        int result = 0;
        List<Integer> shifts = Arrays.asList(-1, 0, 1);
        for (int shiftByX : shifts) {
            for (int shiftByY : shifts) {
                if (shiftByX == 0 && shiftByY == 0) {
                    continue;
                }

                int x = i + shiftByX;
                int y = j + shiftByY;
                if (insideField(x, y)) {
                    if (map.get(x).get(y).equals(map.get(i).get(j))) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    private List<Integer> countAllNeighbors(int i, int j) {
        List<Integer> neighbors = Arrays.asList(new Integer[Manager.CIVILIZATION_NUMBER]);
        for (int k = 0; k < neighbors.size(); k++) {
            neighbors.set(k, 0);
        }
        List<Integer> shifts = Arrays.asList(-1, 0, 1);
        for (int shiftByX : shifts) {
            for (int shiftByY : shifts) {
                if (shiftByX == 0 && shiftByY == 0) {
                    continue;
                }
                int x = i + shiftByX;
                int y = j + shiftByY;
                if (insideField(x, y)) {
                    if (map.get(x).get(y) != 0) {
                        int index = map.get(x).get(y) - 1;
                        neighbors.set(index, neighbors.get(index) + 1);
                    }
                }
            }
        }
        return neighbors;
    }

    private void update() {
        List<List<Integer>> newMap = new ArrayList<>(map);
        for (int i = 0; i < rowsAmount; i++) {
            for (int j = 0; j < columnsAmount; j++) {
                if (map.get(i).get(j) == 0) {
                    List<Integer> neighbors = countAllNeighbors(i, j);
                    int civil = -1;
                    int neighborsAmount = 0;
                    for (int k = 0; k < neighbors.size(); ++k) {
                        if (neighbors.get(k) > neighborsAmount) {
                            neighborsAmount = neighbors.get(k);
                            civil = k;
                        }
                    }
                    if (civil != -1 && neighborsAmount == 3) {
                        newMap.get(i).set(j, civil + 1);
                    }
                } else {
                    int neighborsAmount = countFriendlyNeighbors(i, j);
                    if (neighborsAmount < 2 || neighborsAmount > 3) {
                        newMap.get(i).set(j, 0);
                    }
                }
            }
        }
        map = newMap;
    }

    @Override
    public void run() {
        while (true) {
            cellsMap.putInSecondary(map);
            update();
        }
    }
}
