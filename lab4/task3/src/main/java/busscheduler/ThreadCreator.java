package busscheduler;

import java.util.concurrent.locks.ReadWriteLock;

public class ThreadCreator implements Runnable {
    private ReadWriteLock lock;
    private BusSchedule schedule;
    private Instruction instruction;
    private String firstCity;
    private String secondCity;
    private Integer price;

    public ThreadCreator(ReadWriteLock lock, BusSchedule schedule) {
        this.lock = lock;
        this.schedule = schedule;
    }

    public void addBusStop(String city) throws InterruptedException {
        this.instruction = Instruction.ADD_BUS_STOP;
        this.firstCity = city;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void deleteBusStop(String city) throws InterruptedException {
        this.instruction = Instruction.REMOVE_BUS_STOP;
        this.firstCity = city;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void changeFlightPrice(String firstCity, String secondCity, int price) throws InterruptedException {
        this.instruction = Instruction.CHANGE_PRICE;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.price = price;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void addFlight(String firstCity, String secondCity, int price) throws InterruptedException {
        this.instruction = Instruction.ADD_FLIGHT;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.price = price;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public void deleteFlight(String firstCity, String secondCity) throws InterruptedException {
        this.instruction = Instruction.REMOVE_FLIGHT;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
    }

    public Integer getFlightPrice(String firstCity, String secondCity) throws InterruptedException {
        this.instruction = Instruction.GET_FLIGHT_PRICE;
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
        return price;
    }

    private void addBusStopImpl() {
        lock.writeLock().lock();
        schedule.addBusStop(firstCity);
        lock.writeLock().unlock();
    }

    private void deleteBusStopImpl() {
        lock.writeLock().lock();
        schedule.deleteBusStop(firstCity);
        lock.writeLock().unlock();
    }

    private void changeFlightPriceImpl() {
        lock.writeLock().lock();
        schedule.changeFlightPrice(firstCity, secondCity, price);
        lock.writeLock().unlock();
    }

    private void addFlightImpl() {
        lock.writeLock().lock();
        schedule.addFlight(firstCity, secondCity, price);
        lock.writeLock().unlock();
    }

    private void deleteFlightImpl() {
        lock.writeLock().lock();
        schedule.deleteFlight(firstCity, secondCity);
        lock.writeLock().unlock();
    }

    private void getFlightPriceImpl() {
        lock.writeLock().lock();
        price = schedule.getFlightPrice(firstCity, secondCity);
        lock.writeLock().unlock();
    }

    @Override
    public void run() {
        switch (instruction) {
            case ADD_BUS_STOP: {
                addBusStopImpl();
                break;
            }
            case REMOVE_BUS_STOP: {
                deleteBusStopImpl();
                break;
            }
            case CHANGE_PRICE: {
                changeFlightPriceImpl();
                break;
            }
            case ADD_FLIGHT: {
                addFlightImpl();
                break;
            }
            case REMOVE_FLIGHT: {
                deleteFlightImpl();
                break;
            }
            case GET_FLIGHT_PRICE: {
                getFlightPriceImpl();
                break;
            }
        }
    }
}
