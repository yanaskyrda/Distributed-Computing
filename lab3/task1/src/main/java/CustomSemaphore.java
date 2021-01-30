public class CustomSemaphore {
    private volatile boolean taken = false;

    synchronized void get() {
        while (taken) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }
        taken = true;
    }

    synchronized boolean release() {
        if (taken) {
            taken = false;
            this.notify();
            return true;
        }
        return false;
    }
}
