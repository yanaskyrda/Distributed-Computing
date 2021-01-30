public class Visitor extends Thread {
    private String name;
    private Barber personalBarber;

    public Visitor(String name, Barber personalBarber) {
        this.name = name;
        this.personalBarber = personalBarber;
    }

    @Override
    public void run() {
        getHaircut();
    }

    public void getHaircut() {
        personalBarber.getHaircut(this);
    }

    public String getVisitorName() {
        return name;
    }

    public void changePersonalBarber(Barber personalBarber) {
        this.personalBarber = personalBarber;
    }

    public static void main(String... args) {
        Barber barber = new Barber();
        for (int i = 0; i < 15; i++) {
            new Visitor("Name" + i, barber).start();
        }
    }
}
