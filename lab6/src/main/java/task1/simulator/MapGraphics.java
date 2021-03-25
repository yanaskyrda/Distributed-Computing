package task1.simulator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapGraphics extends JPanel implements Runnable {
    private int cellSize;
    private List<List<Integer>> field;
    private CellsMap cellsMap;
    private List<Color> colors = Arrays.asList(Color.white, Color.orange);

    MapGraphics(CellsMap cellsMap, int cellSize) {
        field = new ArrayList<>(new ArrayList<>());
        this.cellSize = cellSize;
        this.cellsMap = cellsMap;
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < field.size(); i++) {
            for (int j = 0; j < field.get(0).size(); j++) {
                int index = i / Manager.TASK_SIZE + 1;
                if (index > Manager.THREADS_AMOUNT) {
                    index = Manager.THREADS_AMOUNT;
                }
                g.setColor((field.get(i).get(j) != 0) ? colors.get(1) : colors.get(0));
                g.fillRect(j * cellSize, i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            field = new ArrayList<>(new ArrayList<>());
            field.addAll(cellsMap.getFromPrimary());
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
