package zero.programmer.data.kendaraan.model;

public class UpdateRequestBorrowVehicle {

    private String necessity;
    private String borrowDate;
    private String returnDate;
    private String destination;
    private Boolean borrowStatus;

    public UpdateRequestBorrowVehicle() {
    }

    public UpdateRequestBorrowVehicle(String necessity, String borrowDate, String returnDate, String destination, Boolean borrowStatus) {
        this.necessity = necessity;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.destination = destination;
        this.borrowStatus = borrowStatus;
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

    public Boolean getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(Boolean borrowStatus) {
        this.borrowStatus = borrowStatus;
    }
}
