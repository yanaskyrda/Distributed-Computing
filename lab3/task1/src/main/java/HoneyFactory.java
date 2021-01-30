import java.util.ArrayList;
import java.util.List;

public class HoneyFactory {
    private Bear bear;
    private int numberOfBees;
    private HoneyPot honeyPot;

    public HoneyFactory(Bear bear, int numberOfBees, int capacityOfHoneyPot) {
        this.bear = bear;
        this.honeyPot = new HoneyPot(capacityOfHoneyPot);
        this.numberOfBees = numberOfBees;
    }

    private List<Bee> initializeBees() {
        List<Bee> bees = new ArrayList<>(numberOfBees);
        CustomSemaphore semaphore = new CustomSemaphore();
        for (int i = 0; i < numberOfBees; i++) {
            bees.add(new Bee(honeyPot, bear, semaphore));
        }
        return bees;
    }

    public void startFactory() {
        List<Bee> bees = initializeBees();
        honeyPot.emptyThePot();
        for (Bee bee : bees) {
            bee.start();
        }
        while (!honeyPot.isAlreadyEaten());
    }

    public static void main(String... args) {
        Bear WinniThePooh = new Bear();
        HoneyFactory factory = new HoneyFactory(WinniThePooh, 4, 20);
        factory.startFactory();
    }
}
