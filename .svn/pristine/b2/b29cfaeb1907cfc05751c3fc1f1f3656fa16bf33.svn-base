package swd.logistics.bo;

import swd.logistics.po.Sender;

public class PlacingOrderInfo extends BasePOJO {

    private Double OtherCost;//其他费用

    private Sender sender;

    private Receiver Receiver;

    private Commodity Commodity;

    private int IsNotice = 1;//是否需要上门揽件

    private String Remark;//快递订单备注

    private String IsReturnPrintTemplate = "1";//是否返回电子模板，默认为1

    public Double getOtherCost() {
        return OtherCost;
    }

    public void setOtherCost(Double otherCost) {
        OtherCost = otherCost;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return Receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.Receiver = receiver;
    }

    public Commodity getCommodity() {
        return Commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.Commodity = commodity;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getIsReturnPrintTemplate() {
        return IsReturnPrintTemplate;
    }

    public void setIsReturnPrintTemplate(String isReturnPrintTemplate) {
        IsReturnPrintTemplate = isReturnPrintTemplate;
    }

    public int getIsNotice() {
        return IsNotice;
    }

    public void setIsNotice(int isNotice) {
        IsNotice = isNotice;
    }


    @Override
    public String toString() {
        return "PlacingOrderInfo{" +
                ", OtherCost=" + OtherCost +
                ", sender=" + sender +
                ", receiver=" + Receiver +
                ", commodity=" + Commodity +
                ", IsNotice=" + IsNotice +
                ", Remark='" + Remark + '\'' +
                ", IsReturnPrintTemplate='" + IsReturnPrintTemplate + '\'' +
                ", EBusinessID='" + EBusinessID + '\'' +
                ", ShipperCode='" + ShipperCode + '\'' +
                ", LogisticCode='" + LogisticCode + '\'' +
                '}';
    }
}
