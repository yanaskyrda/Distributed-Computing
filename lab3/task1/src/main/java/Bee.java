public class Bee extends Thread {
    private HoneyPot honeyPot;
    private Bear bear;
    private CustomSemaphore semaphore;

    Bee(HoneyPot honeyPot, Bear bear, CustomSemaphore semaphore) {
        this.honeyPot = honeyPot;
        this.bear = bear;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (true) {
            semaphore.get();
            if (honeyPot.isFull() || honeyPot.isAlreadyEaten()) {
                semaphore.release();
                return;
            } else {
                collectHoney();
                if (!honeyPot.addPortion()) {
                    semaphore.release();
                    return;
                }
                if (honeyPot.isFull() && !honeyPot.isAlreadyEaten()) {
                    try {
                        bear.consumeHoney(honeyPot);
                    } catch (NoHoneyException e) {
                        e.printStackTrace();
                    }
                    semaphore.release();
                    return;
                }
                semaphore.release();
            }
        }
    }

    private void collectHoney() {

    }
}
