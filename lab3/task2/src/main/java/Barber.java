import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Barber {
    private Semaphore semaphore = new Semaphore(1);
    private volatile Queue<Visitor> queue = new LinkedList<>();

    public void getHaircut(Visitor visitor) {
        synchronized (queue) {
            queue.offer(visitor);
        }
        try {
            semaphore.acquire();
            doHaircut(queue.remove());
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doHaircut(Visitor visitor) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}
        System.out.println("Barber did a haircut for visitor " + visitor.getVisitorName() + "!");
    }
}
