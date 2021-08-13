package zero.programmer.data.kendaraan.entitites;

import java.util.Date;

public class BorrowVehicle {

    private Integer idBorrow;
    private User user;
    private Vehicle vehicle;
    private Driver driver;
    private String borrowType;
    private Date dateOfFilling;
    private String necessity;
    private Date borrowDate;
    private Date returnDate;
    private String destination;
    private Boolean borrowStatus;

    public BorrowVehicle() {
    }

    public BorrowVehicle(
            Integer idBorrow,
            User user,
            Vehicle vehicle,
            Driver driver,
            String borrowType,
            Date dateOfFilling,
            String necessity,
            Date borrowDate,
            Date returnDate,
            String destination,
            Boolean borrowStatus
    ) {
        this.idBorrow = idBorrow;
        this.user = user;
        this.vehicle = vehicle;
        this.driver = driver;
        this.borrowType = borrowType;
        this.dateOfFilling = dateOfFilling;
        this.necessity = necessity;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.destination = destination;
        this.borrowStatus = borrowStatus;
    }

    public Integer getIdBorrow() {
        return idBorrow;
    }

    public void setIdBorrow(Integer idBorrow) {
        this.idBorrow = idBorrow;
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

    public Date getDateOfFilling() {
        return dateOfFilling;
    }

    public void setDateOfFilling(Date dateOfFilling) {
        this.dateOfFilling = dateOfFilling;
    }

    public String getNecessity() {
        return necessity;
    }

    public void setNecessity(String necessity) {
        this.necessity = necessity;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(Boolean borrowStatus) {
        this.borrowStatus = borrowStatus;
    }
}
