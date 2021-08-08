package zero.programmer.data.kendaraan.entitites;

public class Driver {

    private String idDriver;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Boolean isOnDuty;

    public Driver() {
    }

    public Driver(String idDriver, String fullName, String phoneNumber, String address, Boolean isOnDuty) {
        this.idDriver = idDriver;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isOnDuty = isOnDuty;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getOnDuty() {
        return isOnDuty;
    }

    public void setOnDuty(Boolean onDuty) {
        isOnDuty = onDuty;
    }
}
