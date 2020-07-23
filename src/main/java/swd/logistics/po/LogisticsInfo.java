package swd.logistics.po;

import swd.logistics.bo.BasePOJO;

import java.util.List;

public class LogisticsInfo extends BasePOJO {
    public Long id;

    public Boolean Success;

    public String Reason;

    public String State;

    public String StateEx;//增值服务才有的字段

    public String Location;

    public List<Traces> Traces;

    public Long pushId;


    public String OrderCode;//id,对应数据库的id

    public String Callback;//用来给ordercode赋值

    public String getCallback() {
        return Callback;
    }

    public void setCallback(String callback) {
        Callback = callback;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        Success = success;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public List<swd.logistics.po.Traces> getTraces() {
        return Traces;
    }

    public void setTraces(List<swd.logistics.po.Traces> Traces) {
        Traces = Traces;
    }

    public Long getPushId() {
        return pushId;
    }

    public void setPushId(Long pushId) {
        this.pushId = pushId;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getStateEx() {
        return StateEx;
    }

    public void setStateEx(String stateEx) {
        StateEx = stateEx;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    @Override
    public String toString() {
        return "LogisticsInfo{" +
                "id=" + id +
                ", Success=" + Success +
                ", Reason='" + Reason + '\'' +
                ", State='" + State + '\'' +
                ", StateEx='" + StateEx + '\'' +
                ", Location='" + Location + '\'' +
                ", Traces=" + Traces +
                ", pushId=" + pushId +
                ", OrderCode='" + OrderCode + '\'' +
                ", EBusinessID='" + EBusinessID + '\'' +
                ", ShipperCode='" + ShipperCode + '\'' +
                ", LogisticCode='" + LogisticCode + '\'' +
                '}';
    }
}