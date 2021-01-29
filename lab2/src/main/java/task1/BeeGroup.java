package task1;

public class BeeGroup extends Thread {
    SearchingTask task;
    BeeManager manager;

    BeeGroup(BeeManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while (true) {
            task = manager.getTask();
            if (task == null) {
                return;
            }

            for (int i = task.xStart; i < task.xEnd; i++) {
                for (int j = task.yStart; j < task.yEnd; j++) {
                    if (manager.checkForestCoordinate(i, j) == 1) {
                        manager.setWinniCoordinates(i, j);
                        return;
                    }
                }
            }
        }
    }
}
