public class HoneyPot {
    private volatile int currentOccupancy = 0;
    private volatile boolean alreadyEaten = false;
    private final int capacity;

    HoneyPot(int capacity) {
        this.capacity = capacity;
    }

    synchronized boolean addPortion() {
        if (isFull()) {
            return false;
        }
        currentOccupancy = currentOccupancy + 1;
        return  true;
    }

    synchronized void eat() throws NoHoneyException {
        if (isFull()) {
            currentOccupancy = 0;
            alreadyEaten = true;
        } else {
            throw new NoHoneyException("Honey pot not full!");
        }
    }

    public boolean isFull() {
        return (currentOccupancy == capacity);
    }

    void emptyThePot() {
        this.currentOccupancy = 0;
        this.alreadyEaten = false;
    }

    public boolean isAlreadyEaten() {
        return alreadyEaten;
    }

    public int getCapacity() {
        return capacity;
    }
}
