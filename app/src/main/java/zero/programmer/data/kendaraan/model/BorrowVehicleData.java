package zero.programmer.data.kendaraan.model;

import java.util.Date;

import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.entitites.Vehicle;

public class BorrowVehicleData {
    private User user;
    private Vehicle vehicle;
    private Driver driver;
    private String borrowType;
    private String necessity;
    private String borrowDate;
    private String returnDate;
    private String destination;

    public BorrowVehicleData() {
    }

    public BorrowVehicleData(User user, Vehicle vehicle, Driver driver, String borrowType, String necessity, String borrowDate, String returnDate, String destination) {
        this.user = user;
        this.vehicle = vehicle;
        this.driver = driver;
        this.borrowType = borrowType;
        this.necessity = necessity;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.destination = destination;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getBorrowType() {
        return borrowType;
    }

    public void setBorrowType(String borrowType) {
        this.borrowType = borrowType;
    }

    public String getNecessity() {
        return necessity;
    }

    public void setNecessity(String necessity) {
        this.necessity = necessity;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
