package objects;

import java.util.Objects;

public class Car {
    private final String city;
    private final String brand;
    private final String model;
    private final int year;
    private int state;
    private String fromDate;
    private boolean rented;
    private int DistanceFromOrigin;

    public int getDistanceFromOrigin() {
        return DistanceFromOrigin;
    }

    public void setDistanceFromOrigin(int distanceFromOrigin) {
        DistanceFromOrigin = distanceFromOrigin;
    }

    public Car(String city, String brand, String model, int year, int state) {
        this.city = city;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.state = state;
        this.rented = false;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }


    public String getCity() {
        return this.city;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }



    public void setState(int state) {
        this.state = state;
    }

    public void changeRentedState() {
    this.rented = !this.rented;
    }

    public boolean isRented() {
        return rented;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return year == car.year && city.equals(car.city) && brand.equals(car.brand) && model.equals(car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, brand, model, year, state);
    }

    @Override
    public String toString() {
        return getCity() + ", " + getBrand() + ", " + getModel() + ", " + getYear();
    }
}
