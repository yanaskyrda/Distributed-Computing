package task1.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class CellsMap {
    private List<List<Integer>> primary;
    private List<List<Integer>> secondary;
    private AtomicBoolean wasRead;
    private AtomicBoolean wasWritten;

    CellsMap() {
        primary = new ArrayList<>(new ArrayList<>());;
        secondary = new ArrayList<>(new ArrayList<>());;
        wasRead = new AtomicBoolean(true);
        wasWritten = new AtomicBoolean(false);
    }

    void putInSecondary(List<List<Integer>> data) {
        while (!wasRead.get()) {
            Thread.yield();
        }
        primary.clear();
        primary.addAll(secondary);
        secondary = data;
        wasRead.set(false);
        wasWritten.set(true);
    }

    List<List<Integer>> getFromPrimary() {
        while (!wasWritten.get()) {
            Thread.yield();
        }
        wasWritten.set(false);
        wasRead.set(true);
        return primary;
    }
}
