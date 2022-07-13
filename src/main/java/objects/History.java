package objects;

public class History {
    private final Car car;
    private String fromDate;
    private String dueDate;
    private int state;

    public History(Car car) {
        this.car = car;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFromDate() {
        return fromDate;
    }

    public Car getCar() {
        return car;
    }

    @Override
    public String toString() {
        return fromDate + " - " +
                dueDate + ", " +
                state;
    }
}
