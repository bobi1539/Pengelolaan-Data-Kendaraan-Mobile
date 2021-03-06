package zero.programmer.data.kendaraan.model;

import java.util.Date;

public class VehicleData {
    private String registrationNumber;
    private String name;
    private String merk;
    private String chassisNumber;
    private String machineNumber;
    private String policeNumber;
    private String purchaseDate;
    private Long acquisitionValue;
    private String location;
    private String condition;
    private Boolean isBorrow;

    public VehicleData() {
    }

    public VehicleData(
            String registrationNumber,
            String name,
            String merk,
            String chassisNumber,
            String machineNumber,
            String policeNumber,
            String purchaseDate,
            Long acquisitionValue,
            String location,
            String condition,
            Boolean isBorrow) {
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.merk = merk;
        this.chassisNumber = chassisNumber;
        this.machineNumber = machineNumber;
        this.policeNumber = policeNumber;
        this.purchaseDate = purchaseDate;
        this.acquisitionValue = acquisitionValue;
        this.location = location;
        this.condition = condition;
        this.isBorrow = isBorrow;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getMachineNumber() {
        return machineNumber;
    }

    public void setMachineNumber(String machineNumber) {
        this.machineNumber = machineNumber;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Long getAcquisitionValue() {
        return acquisitionValue;
    }

    public void setAcquisitionValue(Long acquisitionValue) {
        this.acquisitionValue = acquisitionValue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getBorrow() {
        return isBorrow;
    }

    public void setBorrow(Boolean borrow) {
        isBorrow = borrow;
    }
}
