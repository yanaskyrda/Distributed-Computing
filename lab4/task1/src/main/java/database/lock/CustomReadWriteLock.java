package database.lock;

public class CustomReadWriteLock {
    private Integer readLock = 0;
    private Integer writeLock = 0;

    public void readLock() {
        while (readLock == 1) {
            try {
                this.wait(10);
            } catch (InterruptedException ignored) {}
        }
        synchronized (readLock) {
            readLock = 1;
        }
    }

    public void readUnlock() {
        synchronized (readLock) {
            readLock = 0;
        }
    }

    public void writeLock() {
        readLock();
        while (writeLock == 1) {
            try {
                this.wait(10);
            } catch (InterruptedException ignored) {}
        }
        synchronized (writeLock) {
            writeLock = 1;
        }
    }

    public void writeUnlock() {
        synchronized (this) {
            readLock = 0;
            writeLock = 0;
        }
    }
}
