package main.interfaces;

import main.exceptions.InvalidCarException;
import main.objects.Car;

import java.util.ArrayList;

public interface CarRentalInterface {
    public void options();
    public void rent(String city) throws InvalidCarException;
    public void returnCar(String city, String brand, String model, int year, int state) throws InvalidCarException;
    public void availableCars() throws InvalidCarException;
    public void rentedCars() throws InvalidCarException;
    public void getRentalHistory() throws InvalidCarException;
    public ArrayList<Car> readFile();
}
