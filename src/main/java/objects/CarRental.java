package objects;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

import exceptions.InvalidCarException;
import interfaces.CarRentalInterface;
import mapservice.DistanceAnalyzer;

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

    public boolean rent(String city) {
        for (Car car : cars) {
            if (car.getCity().equals(city)) {
                car.changeRentedState();
                History historyInput = new History(car);
                LocalDateTime now = LocalDateTime.now();
                historyInput.setFromDate(dtf.format(now));
                car.setFromDate(historyInput.getFromDate());
                rentalHistory.add(historyInput);
                return true;
            }
        }
        return false;
    }

    public void returnCar(String city, String brand, String model, int year, int state) throws InvalidCarException {
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

    public Car findNearestCar(String city) {
        DistanceAnalyzer analyzer = new DistanceAnalyzer();
        Car nearestCar = null;
        for (Car car : cars) {
            if (!(car.isRented())) {
                nearestCar = car;
                break;
            } else {
                System.out.println("Nie ma dostępnych samochodów");
            }
        }
        for (Car value : cars) {
            if (!(value.isRented())) {
                int distance = analyzer.calculateDistance(city, value.getCity());
                value.setDistanceFromOrigin(distance);
                assert nearestCar != null;
                if (value.getDistanceFromOrigin() < nearestCar.getDistanceFromOrigin()) {
                    nearestCar = value;

                }
            }
        }
        assert nearestCar != null;
        return nearestCar;
    }

    public void rentNearestCar(Car car){
        rent(car.getCity());
    }

    public ArrayList<Car> readFile() {
        ArrayList<Car> cars = new ArrayList<>();

        InputStream file = this.getClass().getResourceAsStream("/resources/cars.txt");
        try {
            InputStreamReader fileScan = new InputStreamReader(Objects.requireNonNull(file), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(fileScan);
            String temp = reader.readLine();
            while (temp != null) {
                String[] parts = temp.split(", ");
                Car car = new Car(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
                cars.add(car);
                temp = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cars;
    }
}
