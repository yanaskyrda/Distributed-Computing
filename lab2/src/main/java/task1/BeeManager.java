package task1;

import java.util.ArrayList;
import java.util.List;

public class BeeManager {
    private List<List<Integer>> forest = new ArrayList<>();
    private List<BeeGroup> beeForces = new ArrayList<>(3);
    private volatile List<SearchingTask> tasks = new ArrayList<>();
    private volatile Integer WinniX = null;
    private volatile Integer WinniY = null;

    private BeeManager(int forestWidth, int forestHeight, int numberOfForestSquares, int numberOfBeeGroups) {
        initializeForest(forestWidth, forestHeight);
        initializeTasks(numberOfForestSquares);
        for (int i = 0; i < numberOfBeeGroups; i++) {
            beeForces.add(new BeeGroup(this));
        }
    }

    private void initializeForest(int width, int height) {
        for (int i = 0; i < height; i++) {
            List<Integer> row = new ArrayList<>(width);
            for (int j = 0; j < width; j++) {
                if (i == WinniThePooh.getY() && j == WinniThePooh.getX()) {
                    row.add(1);
                } else {
                    row.add(0);
                }
            }
            forest.add(row);
        }
    }

    private void initializeTasks(int numberOfForestSquares) {
        int height = forest.size();
        int width = forest.get(0).size();
        int heightOfSquare = height / numberOfForestSquares;
        int widthOfSquare = width / numberOfForestSquares;
        for (int i = 0; i < numberOfForestSquares; i++) {
            for (int j = 0; j < numberOfForestSquares; j++) {
                tasks.add(new SearchingTask(widthOfSquare * i,
                        widthOfSquare * (i + 1),
                        heightOfSquare * j,
                        heightOfSquare * (j + 1)));
            }
        }
    }

    int checkForestCoordinate(int x, int y) {
        return forest.get(y).get(x);
    }

    synchronized SearchingTask getTask() {
        if (tasks.size() == 0 || WinniX != null) {
            for (BeeGroup beeGroup : beeForces) {
                beeGroup.interrupt();
            }
            return null;
        }
        SearchingTask taskToDo = tasks.get(tasks.size() - 1);
        tasks.remove(taskToDo);
        return taskToDo;
    }

    private void searchForWinni() {
        for (BeeGroup beeGroup : beeForces) {
            beeGroup.start();
        }
        while (WinniX == null);
    }

    void setWinniCoordinates(int x, int y) {
        this.WinniX = x;
        this.WinniY = y;
    }

    public  Integer getWinniX() {
        if (WinniX == null) {
            searchForWinni();
        }
        return WinniX;
    }

    public Integer getWinniY() {
        if (WinniY == null) {
            searchForWinni();
        }
        return WinniY;
    }

    public static void main(String... args) {
        WinniThePooh.setCoordinates(38, 56);
        BeeManager manager = new BeeManager(100, 100, 10,
                3);
        System.out.println("x = " + manager.getWinniX() + " y = " + manager.getWinniY());
    }
}
