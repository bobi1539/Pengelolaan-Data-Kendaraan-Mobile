package zero.programmer.data.kendaraan.entitites;

import zero.programmer.data.kendaraan.utils.RoleId;

public class User {

    private String username;
    private String password;
    private String fullName;
    private String employeeNumber;
    private String position;
    private String workUnit;
    private RoleId roleId;

    public User() {
    }

    public User(
            String username,
            String password,
            String fullName,
            String employeeNumber,
            String position,
            String workUnit,
            RoleId roleId
    ) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.employeeNumber = employeeNumber;
        this.position = position;
        this.workUnit = workUnit;
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public RoleId getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleId roleId) {
        this.roleId = roleId;
    }
}
