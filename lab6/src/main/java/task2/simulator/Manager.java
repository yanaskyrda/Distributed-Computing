package task2.simulator;

import javax.swing.JFrame;
import java.awt.*;

public class Manager {
    static final int CIVILIZATION_NUMBER = 3;
    static final int MAP_SIZE = 50;
    static final int THREADS_AMOUNT = 5;
    static final int TASK_SIZE = 10;
    private int cellSize;
    
    
    public Manager(int cellSize) {
        this.cellSize = cellSize;
    }

    private void initializeFrame(JFrame frame, Component component) {
        frame.getContentPane().add(component);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize((MAP_SIZE) * cellSize, MAP_SIZE * cellSize);
        frame.setTitle("life simulator");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void startLife() {
        CellsMap cellsMap = new CellsMap();
        Simulation simulation = new Simulation(cellsMap, MAP_SIZE, MAP_SIZE);
        MapGraphics map = new MapGraphics(cellsMap, cellSize);

        JFrame frame = new JFrame();
        initializeFrame(frame, map);

        new Thread(simulation).start();
        new Thread(map).start();
        while (true) {
            Thread.yield();
        }
    }
}
