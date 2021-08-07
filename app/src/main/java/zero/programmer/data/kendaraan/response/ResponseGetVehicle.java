package zero.programmer.data.kendaraan.response;

import java.util.List;

import zero.programmer.data.kendaraan.entitites.Vehicle;

public class ResponseGetVehicle {
    private Integer code;
    private String status;
    private List<String> messages;
    private Vehicle data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Vehicle getData() {
        return data;
    }

    public void setData(Vehicle data) {
        this.data = data;
    }
}
