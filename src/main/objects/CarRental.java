package main.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import static java.lang.Integer.parseInt;
import main.exceptions.*;
import main.interfaces.CarRentalInterface;

public class CarRental implements CarRentalInterface {
    final DateTimeFormatter dtf;
    private final ArrayList<Car> cars;
    private final ArrayList<History> rentalHistory;


    public CarRental() {
        this.rentalHistory = new ArrayList<>();
        this.cars = readFile();
        this.dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    }


    public void options() {
        System.out.println(
                "\n1 - Wypożyczam auto" +
                        "\n2 - Oddaję auto" +
                        "\n3 - Lista dostępnych aut" +
                        "\n4 - Lista wypożyczonych aut" +
                        "\n5 - Historia wypożyczeń" +
                        "\n6 - Kończymy na dziś");
    }

    public void rent(String city) throws InvalidCarException {
        if (cars.stream().noneMatch(c -> c.getCity().equals(city))) {
            throw new InvalidCarException("Przepraszamy, w tym mieście nie ma dostępnych samochodów.");
        }
        for (Car car : cars) {
            if (car.getCity().equals(city)) {
                car.changeRentedState();
                History historyInput = new History(car);
                LocalDateTime now = LocalDateTime.now();
                historyInput.setFromDate(dtf.format(now));
                car.setFromDate(historyInput.getFromDate());
                rentalHistory.add(historyInput);
                break;
            }
        }
    }

    public void returnCar(String city, String brand, String model, int year, int state) throws InvalidCarException{
        for (History history : rentalHistory) {
            if (history.getCar().getCity().equalsIgnoreCase(city)
                    && history.getCar().getBrand().equalsIgnoreCase(brand)
                    && history.getCar().getModel().equalsIgnoreCase(model)
                    && history.getCar().getYear() == year
                    && history.getFromDate().equals(history.getCar().getFromDate())) {
                LocalDateTime now = LocalDateTime.now();
                history.setDueDate(dtf.format(now));
                history.setState(state);
            } else {
                throw new InvalidCarException("Nie ma takiego samochodu.");
            }
        }
        for (Car car : cars) {
            if (car.getCity().equalsIgnoreCase(city)
                    && car.getBrand().equalsIgnoreCase(brand)
                    && car.getModel().equalsIgnoreCase(model)
                    && car.getYear() == year) {
                car.setState(state);
                car.changeRentedState();
            } else {
                throw new InvalidCarException("Nie ma takiego samochodu.");
            }
        }
    }

    public void availableCars() throws InvalidCarException {
        if (cars.isEmpty()) {
            throw new InvalidCarException("Nie ma dostępnych samochodów");
        } else {
            cars.stream()
                    .filter(Predicate.not(Car::isRented))
                    .sorted(Comparator.comparing(Car::getCity)
                            .thenComparingInt(Car::getYear))
                    .forEach(System.out::println);
        }
    }


    public void rentedCars() throws InvalidCarException {
        if (cars.stream().noneMatch(Car::isRented)) {
            throw new InvalidCarException("Wszystkie samochody są dostępne");
        } else {
            cars.stream()
                    .filter(Car::isRented)
                    .forEach(System.out::println);
        }
    }
    public void getRentalHistory() throws InvalidCarException {
        if (rentalHistory.isEmpty()) {
            throw new InvalidCarException("Historia jest pusta.");
        } else {
            for (History history : rentalHistory) {
                System.out.println(history.getCar());
                System.out.println(history);
            }
        }
    }

    public ArrayList<Car> readFile() {
        ArrayList<Car> cars = new ArrayList<>();
        String file = "resources/cars.txt";
        try {
            Scanner fileScan = new Scanner(new File(file));
            while (fileScan.hasNextLine()) {
                String output = fileScan.nextLine();
                String[] parts = output.split(", ");
                Car car = new Car(parts[0], parts[1], parts[2], parseInt(parts[3]), parseInt(parts[4]));
                cars.add(car);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cars;
    }
}
