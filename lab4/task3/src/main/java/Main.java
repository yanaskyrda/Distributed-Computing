import busscheduler.BusSchedule;
import busscheduler.ThreadCreator;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    private static final String KYIV = "Kyiv";
    private static final String LVIV = "Lviv";
    private static final String CHERKASY = "Cherkasy";
    private static final String KHARKIV = "Kharkiv";

    public static void main(String... args) throws InterruptedException {
        BusSchedule schedule = new BusSchedule();
        ThreadCreator threadCreator = new ThreadCreator(new ReentrantReadWriteLock(false), schedule);
        threadCreator.addBusStop(KYIV);
        threadCreator.addBusStop(LVIV);
        threadCreator.addBusStop(CHERKASY);
        threadCreator.addBusStop(KHARKIV);
        threadCreator.addFlight(KYIV, LVIV, 150);
        threadCreator.addFlight(CHERKASY, LVIV, 170);
        threadCreator.addFlight(CHERKASY, KHARKIV, 120);
        threadCreator.addFlight(KYIV, CHERKASY, 50);
        threadCreator.changeFlightPrice(KYIV, CHERKASY, 80);
        System.out.println("Price for flight " + CHERKASY + " - " + KHARKIV + " = " +
                threadCreator.getFlightPrice(CHERKASY, KHARKIV));
        threadCreator.deleteFlight(CHERKASY, KYIV);
        threadCreator.deleteBusStop(LVIV);
    }
}
