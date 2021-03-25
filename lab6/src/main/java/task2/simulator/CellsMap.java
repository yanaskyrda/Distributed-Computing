package task2.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class CellsMap {
    private List<List<Integer>> primary;
    private List<List<Integer>> secondary;
    private AtomicBoolean alreadyRead;
    private AtomicBoolean alreadyWritten;

    CellsMap() {
        primary = new ArrayList<>(new ArrayList<>());
        ;
        secondary = new ArrayList<>(new ArrayList<>());
        alreadyRead = new AtomicBoolean(true);
        alreadyWritten = new AtomicBoolean(false);
    }

    void putInSecondary(List<List<Integer>> data) {
        while (!alreadyRead.get()) {
            Thread.yield();
        }
        primary.clear();
        primary.addAll(secondary);
        secondary = data;
        alreadyRead.set(false);
        alreadyWritten.set(true);
    }

    List<List<Integer>> getFromPrimary() {
        while (!alreadyWritten.get()) {
            Thread.yield();
        }
        alreadyWritten.set(false);
        alreadyRead.set(true);
        return primary;
    }
}
