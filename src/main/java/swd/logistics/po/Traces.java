package swd.logistics.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Traces {
    public Long tracesId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date AcceptTime;

    public String AcceptStation;

    public Long logisticsInfoId;

    public String Remark;

    public String Location;

    public String Action;//状态同StateEx

    public Long getTracesId() {
        return tracesId;
    }

    public void setTracesId(Long tracesId) {
        this.tracesId = tracesId;
    }

    public Date getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        AcceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return AcceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        AcceptStation = acceptStation;
    }

    public Long getLogisticsInfoId() {
        return logisticsInfoId;
    }

    public void setLogisticsInfoId(Long logisticsInfoId) {
        this.logisticsInfoId = logisticsInfoId;
    }


    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }


    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    @Override
    public String toString() {
        return "Traces{" +
                "tracesId=" + tracesId +
                ", AcceptTime=" + AcceptTime +
                ", AcceptStation='" + AcceptStation + '\'' +
                ", logisticsInfoId=" + logisticsInfoId +
                ", Remark='" + Remark + '\'' +
                ", Location='" + Location + '\'' +
                ", Action='" + Action + '\'' +
                '}';
    }
}