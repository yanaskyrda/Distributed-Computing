public class Bear {
    public void consumeHoney(HoneyPot honeyPot) throws NoHoneyException {
        honeyPot.eat();
        System.out.println("Bear consumer all honey from the honey pot with capacity " + honeyPot.getCapacity());
    }
}
