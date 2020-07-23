package swd.logistics.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import swd.logistics.po.LogisticsInfo;

import java.util.Date;
import java.util.List;

public class PushLogisticsInfo{

    public String Count;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date PushTime;

    public List<LogisticsInfo> Data;

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public Date getPushTime() {
        return PushTime;
    }

    public void setPushTime(Date pushTime) {
        PushTime = pushTime;
    }

    public List<LogisticsInfo> getData() {
        return Data;
    }

    public void setData(List<LogisticsInfo> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "PushLogisticsInfo{" +
                "Count='" + Count + '\'' +
                ", PushTime=" + PushTime +
                ", Data=" + Data +
                '}';
    }
}
