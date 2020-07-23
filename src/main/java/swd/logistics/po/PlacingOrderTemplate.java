package swd.logistics.po;

import java.util.Date;
import java.util.List;

public class PlacingOrderTemplate {
    private Long id;

    private String orderId;

    private Date updateTime;

    private String printtemplate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPrinttemplate() {
        return printtemplate;
    }

    public void setPrinttemplate(String printtemplate) {
        this.printtemplate = printtemplate == null ? null : printtemplate.trim();
    }


    @Override
    public String toString() {
        return "PlacingOrderTemplate{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", updateTime=" + updateTime +
                ", printtemplate='" + printtemplate + '\'' +
                '}';
    }
}