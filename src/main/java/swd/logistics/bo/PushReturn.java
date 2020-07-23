package swd.logistics.bo;

import java.io.Serializable;
import java.util.Date;

public class PushReturn  {

    public String EBusinessID;

    public Date UpdateTime;

    public Boolean Success;

    public String Reason;

    public Date getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(Date updateTime) {
        UpdateTime = updateTime;
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

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    @Override
    public String toString() {
        return "PushReturn{" +
                "UpdateTime=" + UpdateTime +
                ", Success=" + Success +
                ", Reason='" + Reason + '\'' +
                ", EBusinessID='" + EBusinessID + '\'' +
                '}';
    }
}
