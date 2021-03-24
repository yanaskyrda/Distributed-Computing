package task1.simulator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import javax.swing.*;

public class Manager {
    static final int CIVILIZATION_NUMBER = 1;
    static final int MAP_SIZE = 50;
    static final int THREADS_AMOUNT = 5;
    static final int TASK_SIZE = 10;
    static volatile boolean updated = true;
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
        CyclicBarrier barrier = new CyclicBarrier(THREADS_AMOUNT + 1, () -> updated = false);
        Simulation simulation = new Simulation(cellsMap, barrier, MAP_SIZE, MAP_SIZE);
        MapGraphics map = new MapGraphics(cellsMap, cellSize);
        JFrame frame = new JFrame();
        initializeFrame(frame, map);

        List<Thread> threads = new ArrayList<>(THREADS_AMOUNT);
        int generated = 0;
        for (int i = 0; i < THREADS_AMOUNT - 1; ++i) {
            threads.add(new Thread(new Civilization(barrier, simulation, generated, TASK_SIZE)));
            generated += TASK_SIZE;
        }
        threads.add(new Thread(
                new Civilization(barrier, simulation, generated, MAP_SIZE - generated)));

        new Thread(simulation).start();
        new Thread(map).start();

        for (int i = 0; i < THREADS_AMOUNT; ++i) {
            threads.get(i).start();
        }
        while (true) {
            Thread.yield();
        }
    }
}
