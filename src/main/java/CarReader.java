import objects.Car;
import objects.CarRental;
import exceptions.InvalidCarException;
import mapservice.DistanceAnalyzer;

import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.util.*;

import static java.lang.Integer.parseInt;

public class CarReader {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarRental rental = new CarRental();
        System.out.println("Witaj w wypożyczalni samochodów! Dostępne opcje:");
        while (true) {
            rental.options();
            String input = scanner.nextLine();
            if (input.equals("1")) {
                System.out.println("Jakie miasto?");
                String city = scanner.nextLine();
                if (!rental.rent(city)) {
                    Car nearestCar = rental.findNearestCar(city);
                    System.out.println("Najbliższy samochód jest w mieście " + nearestCar.getCity() + ", które jest oddalone o " + nearestCar.getDistanceFromOrigin() + " km. Czy chcesz go wypożyczyć? Y/N");
                    if (scanner.nextLine().equalsIgnoreCase("Y")) {
                        rental.rentNearestCar(nearestCar);
                    }
                }
                }
                if (input.equals("2")) {
                System.out.println("Podaj miasto:");
                String city = scanner.nextLine();
                System.out.println("Markę:");
                String brand = scanner.nextLine();
                System.out.println("Model:");
                String model = scanner.nextLine();
                System.out.println("Rok:");
                int year = parseInt(scanner.nextLine());
                System.out.println("Stan licznika na dzień dzisiejszy?");
                int state = parseInt(scanner.nextLine());
                try {
                    rental.returnCar(city.trim(), brand.trim(), model.trim(), year, state);
                } catch (InvalidCarException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (input.equals("3")) {
                try {
                    rental.availableCars();
                } catch (InvalidCarException e) {
                    System.out.println(e.getMessage());
                }
                }
                if (input.equals("4")) {
                    try {
                        rental.rentedCars();
                    } catch(InvalidCarException e){
                        System.out.println(e.getMessage());
                    }
                }
                if (input.equals("5")) {
                    try {
                        rental.getRentalHistory();
                    } catch(InvalidCarException e){
                        System.out.println(e.getMessage());
                    }
                }
                if (input.equals("6")) {
                    break;
                }
            }
        }
    }