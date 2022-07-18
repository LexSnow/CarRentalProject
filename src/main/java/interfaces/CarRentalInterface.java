package interfaces;

import exceptions.InvalidCarException;
import objects.Car;

import java.util.ArrayList;

public interface CarRentalInterface {
    void options();
    boolean rent(String city) throws InvalidCarException;
    void returnCar(String city, String brand, String model, int year, int state) throws InvalidCarException;
    void availableCars() throws InvalidCarException;
    void rentedCars() throws InvalidCarException;
    void getRentalHistory() throws InvalidCarException;
    ArrayList<Car> readFile();
}
